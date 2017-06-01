package org.ebike.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queries.function.ValueSource;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.apache.lucene.spatial.query.SpatialOperation;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

/**
 * 
 * @ClassName: SpatialIndexHelper 
 * @Description: 基于Lucene的地理位置检索辅助类
 * @author hzjintianfan
 * @date 2014-7-23 下午7:28:50
 */
public class SpatialIndexHelper
{
	private static SpatialContext ctx;
	private static SpatialStrategy strategy;
	private static Directory directory;
	private static Integer MAX_HITS = 50;

	static
	{
		ctx = SpatialContext.GEO;
		int maxLevels = 11;// results in sub-meter precision for geohash
		SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);
		strategy = new RecursivePrefixTreeStrategy(grid, "myGeoField");
		try
		{
			directory = FSDirectory.open(new File("E:/eight/index"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static Double calDistance(Double x1, Double y1, Double x2, Double y2)
	{
		Point pt = ctx.makePoint(x1, y1);
		double doc1DistDEG = ctx.calcDistance(pt, x2, y2);
		return DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);
	}

	public static Document getSpatialDocument(SpatialData data)
	{
		Document doc = new Document();
		doc.add(new StringField("id", data.getId(), Field.Store.YES));
		doc.add(new StringField("address", data.getAddress(), Field.Store.YES));
		Shape shape = ctx.makePoint(data.getX(), data.getY());
		for (IndexableField f : strategy.createIndexableFields(shape))
		{
			doc.add(f);
		}
		doc.add(new StoredField(strategy.getFieldName(), data.getX() + " " + data.getY()));
		return doc;
	}

	public static void addIndex(SpatialData data) throws IOException
	{
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_48, null);
		IndexWriter indexWriter = new IndexWriter(directory, iwConfig);
		indexWriter.addDocument(getSpatialDocument(data));
		indexWriter.close();
	}

	public static void addIndex(List<SpatialData> datas) throws IOException
	{
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_48, null);
		IndexWriter indexWriter = new IndexWriter(directory, iwConfig);
		for (SpatialData data : datas)
		{
			indexWriter.addDocument(getSpatialDocument(data));
		}
		indexWriter.close();
	}

	/**
	 * 搜索指定半径内的地址，按照距离远近排序返回
	 * @param x 经度
	 * @param y 纬度
	 * @param distance 半径，单位为km
	 * @return
	 * @throws IOException 
	 */
	public static List search(double x, double y, double distance) throws IOException
	{
		List list = new ArrayList();
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Point pt = ctx.makePoint(x, y);
		ValueSource valueSource = strategy.makeDistanceValueSource(pt, DistanceUtils.DEG_TO_KM);// the distance (in km)
		Sort distSort = new Sort(valueSource.getSortField(false)).rewrite(indexSearcher);// false=asc dist

		//增加半径过滤器
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, ctx.makeCircle(pt,
				DistanceUtils.dist2Degrees(distance, DistanceUtils.EARTH_MEAN_RADIUS_KM)));
		Filter filter = strategy.makeFilter(args);
		TopDocs docs = indexSearcher.search(new MatchAllDocsQuery(), filter, MAX_HITS, distSort);
		for (ScoreDoc scoreDoc : docs.scoreDocs)
		{
			Document doc = indexSearcher.doc(scoreDoc.doc);
			String doc1Str = doc.getField(strategy.getFieldName()).stringValue();
			int spaceIdx = doc1Str.indexOf(' ');
			double xx = Double.parseDouble(doc1Str.substring(0, spaceIdx));
			double yy = Double.parseDouble(doc1Str.substring(spaceIdx + 1));
			double doc1DistDEG = ctx.calcDistance(pt, xx, yy);
			double d = DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);

			Object[] o = new Object[4];
			o[0] = doc.getField("id").stringValue();
			;
			o[1] = doc.getField("address").stringValue();
			o[2] = doc.getField(strategy.getFieldName()).stringValue();
			o[3] = d;
			list.add(o);
		}
		indexReader.close();
		return list;
	}

	public static void showResults(List list)
	{
		for (Object item : list)
		{
			Object[] o = (Object[]) item;
			System.out.println(o[0] + " " + o[1] + " " + o[2] + " " + o[3]);
		}
	}

	public static void main(String[] args)
	{
		Date now = new Date();
		for (int i = 0; i < 10; i++)
		{
			System.out.println(SpatialIndexHelper.calDistance(116.405131, 39.915488, 116.372648, 39.920026));
		}
		System.out.println("cost: " + (new Date().getTime() - now.getTime()));
	}
}
