package com.priyanathbhukta.notenest.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.util.CommonUtil;

public class AuditAwareConfig implements AuditorAware<Integer> {

	@Override
	public Optional<Integer> getCurrentAuditor() {
		User loggedInUser = CommonUtil.getLoggedInUser();
		return Optional.of(loggedInUser.getId());
	}
	
}
