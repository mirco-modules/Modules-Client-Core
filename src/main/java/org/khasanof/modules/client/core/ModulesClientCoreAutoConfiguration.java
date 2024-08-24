package org.khasanof.modules.client.core;

import org.khasanof.modules.client.core.config.ModulesClientCoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * @author Nurislom
 * @see org.khasanof.modules.client.core
 * @since 8/24/2024 6:39 PM
 */
@EnableConfigurationProperties({ModulesClientCoreProperties.class})
@ComponentScan(excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class ModulesClientCoreAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ModulesClientCoreAutoConfiguration.class);
}
