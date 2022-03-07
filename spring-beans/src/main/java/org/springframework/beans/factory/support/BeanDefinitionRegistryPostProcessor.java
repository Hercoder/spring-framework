/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * Extension to the standard {@link BeanFactoryPostProcessor} SPI, allowing for
 * the registration of further bean definitions <i>before</i> regular
 * BeanFactoryPostProcessor detection kicks in. In particular,
 * BeanDefinitionRegistryPostProcessor may register further bean definitions
 * which in turn define BeanFactoryPostProcessor instances.
 *
 * BeanDefinitionRegistryPostProcessor继承了BeanFactoryPostProcessor，同时具有自己的新方法。
 *
 * BeanDefinitionRegistryPostProcessor后处理器用于我们自定义的添加更多的beandefinition。
 * 它非常的有用，比如dubbo自己的service扫描，就是用了一个ServiceAnnotationBeanPostProcessor后处理器，
 * 将指定路径下具有Dubbo的@service注解的类添加到bean定义中，
 * 又比如mybatis的basePackage扫描，就是使用MapperScannerConfigurer后处理器，将指定目录下的mapper接口添加到bean定义中。
 *
 * @author Juergen Hoeller
 * @since 3.0.1
 * @see org.springframework.context.annotation.ConfigurationClassPostProcessor
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {

	/**
	 * Modify the application context's internal bean definition registry after its
	 * standard initialization. All regular bean definitions will have been loaded,
	 * but no beans will have been instantiated yet. This allows for adding further
	 * bean definitions before the next post-processing phase kicks in.
	 * @param registry the bean definition registry used by the application context
	 * @throws org.springframework.beans.BeansException in case of errors
	 *
	 * 在应用程序上下文的标准初始化后修改其内部 bean 定义注册表。所有常规 bean 定义都将已加载，但尚未实例化任何 bean。
	 * 这允许在下一个后处理阶段开始之前添加更多的 bean 定义。
	 */
	void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;

}
