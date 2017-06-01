package org.ebike.security;

import java.util.ArrayList;
import java.util.List;

import org.ebike.entity.Account;
import org.ebike.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * @ClassName: MyUserDeitailsService 
 * @Description: 用户权限数据加载类
 * @author hzjintianfan
 * @date 2014-7-23 下午6:45:29
 */
public class MyUserDeitailsService implements UserDetailsService
{

	@Autowired
	private AccountService accountService;// 账号管理接口
	private List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	/**
	 * 根据账号名生成权限对象
	 */
	@Override
	@SuppressWarnings(
	{})
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException
	{
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
		Account account = accountService.getAccountByUsername(username);
		if (account == null)
			throw new UsernameNotFoundException("User name is not found.");
		else
		{// 如果账号存在，则查找其拥有的权限，并打包成security需要的形式
			if (account.getType().equals(1))
			{
				authList.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
			}
			else if (account.getType().equals(0))
			{
				authList.add(new GrantedAuthorityImpl("ROLE_USER"));
			}
			return new org.springframework.security.core.userdetails.User(account.getUsername(), account.getPassword(),
					true, true, true, true, authList);
		}
	}

}
