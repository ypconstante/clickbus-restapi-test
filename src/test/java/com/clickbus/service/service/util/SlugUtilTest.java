package com.clickbus.service.service.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class SlugUtilTest {

	@Test
	public void testMakeSlug() {
		assertThat(SlugUtil.makeSlug("")).isEqualTo("");
		assertThat(SlugUtil.makeSlug("TESTE")).isEqualTo("teste");
		assertThat(SlugUtil.makeSlug("teste")).isEqualTo("teste");
		assertThat(SlugUtil.makeSlug("TesteSlug")).isEqualTo("testeslug");
		assertThat(SlugUtil.makeSlug("teste slug")).isEqualTo("teste-slug");
		assertThat(SlugUtil.makeSlug("TesteSlug em Ação")).isEqualTo("testeslug-em-acao");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testMakeSlugNullParameter() {
		assertThat(SlugUtil.makeSlug(null)).isEqualTo(null);
	}

}
