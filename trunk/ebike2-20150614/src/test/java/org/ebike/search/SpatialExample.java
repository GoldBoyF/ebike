package org.ebike.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Shape;

public class SpatialExample
{

	public static void main(String[] args) throws Exception
	{
		new SpatialExample().test();
	}

	public void test() throws Exception
	{
		init();
		indexPoints();
		search();
	}

	private SpatialContext ctx;// "ctx" is the conventional variable name

	private SpatialStrategy strategy;

	private Directory directory;

	protected void init()
	{
		ctx = SpatialContext.GEO;
		int maxLevels = 11;// results in sub-meter precision for geohash
		SpatialPrefixTree grid = new GeohashPrefixTree(ctx, maxLevels);
		strategy = new RecursivePrefixTreeStrategy(grid, "myGeoField");
		directory = new RAMDirectory();
	}

	private void indexPoints() throws Exception
	{
		IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_48, null);
		IndexWriter indexWriter = new IndexWriter(directory, iwConfig);

		// Spatial4j is x-y order for arguments
		indexWriter.addDocument(newSampleDocument(1, "西城区", ctx.makePoint(116.372648, 39.920026)));
		indexWriter.addDocument(newSampleDocument(2, "朝阳区", ctx.makePoint(116.476133, 39.963396)));
		indexWriter.addDocument(newSampleDocument(3, "东城区", ctx.makePoint(116.421516, 39.935519)));
		indexWriter.addDocument(newSampleDocument(4, "海定区", ctx.makePoint(116.301646, 39.966161)));
		indexWriter.addDocument(newSampleDocument(5, "石景山区", ctx.makePoint(116.230357, 39.912168)));
		indexWriter.addDocument(newSampleDocument(6, "丰台区", ctx.makePoint(116.295897, 39.865664)));
		indexWriter.close();
	}

	private Document newSampleDocument(int id, String name, Shape... shapes)
	{
		Document doc = new Document();
		doc.add(new IntField("id", id, Field.Store.YES));
		doc.add(new StringField("address", name, Field.Store.YES));
		// Potentially more than one shape in this field is supported by some
		// strategies; see the javadocs of the SpatialStrategy impl to see.
		for (Shape shape : shapes)
		{
			for (IndexableField f : strategy.createIndexableFields(shape))
			{
				doc.add(f);
			}
			// store it too; the format is up to you
			// (assume point in this example)
			Point pt = (Point) shape;
			doc.add(new StoredField(strategy.getFieldName(), pt.getX() + " " + pt.getY()));
		}

		return doc;
	}

	private void search() throws Exception
	{
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		Point pt = ctx.makePoint(116.405131, 39.915488);
		ValueSource valueSource = strategy.makeDistanceValueSource(pt, DistanceUtils.DEG_TO_KM);// the distance (in km)
		Sort distSort = new Sort(valueSource.getSortField(false)).rewrite(indexSearcher);// false=asc dist

		//增加半径过滤器
		SpatialArgs args = new SpatialArgs(SpatialOperation.Intersects, ctx.makeCircle(pt,
				DistanceUtils.dist2Degrees(3, DistanceUtils.EARTH_MEAN_RADIUS_KM)));
		Filter filter = strategy.makeFilter(args);
		TopDocs docs = indexSearcher.search(new MatchAllDocsQuery(), filter, 10, distSort);
		for (ScoreDoc scoreDoc : docs.scoreDocs)
		{
			Document doc = indexSearcher.doc(scoreDoc.doc);
			String doc1Str = doc.getField(strategy.getFieldName()).stringValue();
			// assume doc1Str is "x y" as written in newSampleDocument()
			int spaceIdx = doc1Str.indexOf(' ');
			double x = Double.parseDouble(doc1Str.substring(0, spaceIdx));
			double y = Double.parseDouble(doc1Str.substring(spaceIdx + 1));
			double doc1DistDEG = ctx.calcDistance(pt, x, y);
			double d = DistanceUtils.degrees2Dist(doc1DistDEG, DistanceUtils.EARTH_MEAN_RADIUS_KM);
			System.out.println(doc.getField("address").stringValue() + "  "
					+ doc.getField(strategy.getFieldName()).stringValue() + "  " + d);
		}
		indexReader.close();
	}

}