package com.grasstudy.group;

import com.grasstudy.group.entity.StudyMember;
import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.convert.ConverterBuilder;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableR2dbcRepositories(basePackages = "com.grasstudy.group.repository")
public class R2DBCConfiguration extends AbstractR2dbcConfiguration {

	@Bean
	public H2ConnectionFactory connectionFactory() {
		return new H2ConnectionFactory(
				H2ConnectionConfiguration.builder()
				                         .url("mem:testdb;DB_CLOSE_DELAY=-1;")
				                         .username("sa")
				                         .build()
		);
	}

	@Bean
	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
		ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("table-schema.sql")));
		return initializer;
	}

	@Override
	protected List<Object> getCustomConverters() {
		List<Object> customConverters = new ArrayList<>();
		customConverters.addAll(
				ConverterBuilder
						.reading(String.class, StudyMember.Authority.class, StudyMember.Authority::valueOf)
						.andWriting(StudyMember.Authority::toString)
						.getConverters()
		);
		return customConverters;
	}

}
