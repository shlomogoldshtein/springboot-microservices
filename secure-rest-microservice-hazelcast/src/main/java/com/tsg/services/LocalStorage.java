package com.tsg.services;

import org.springframework.stereotype.Service;

/**
 * @author Shlomo Goldshtein
 * bean to 
 * just store in memory things that are just for this cluster node
 * 
 *
 */
@Service
public class LocalStorage {
	String securityToken;

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}
}
