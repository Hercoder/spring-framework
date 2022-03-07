/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.context.support;

import java.io.IOException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

/**
 * Convenient base class for {@link org.springframework.context.ApplicationContext}
 * implementations, drawing configuration from XML documents containing bean definitions
 * understood by an {@link org.springframework.beans.factory.xml.XmlBeanDefinitionReader}.
 *
 * <p>Subclasses just have to implement the {@link #getConfigResources} and/or
 * the {@link #getConfigLocations} method. Furthermore, they might override
 * the {@link #getResourceByPath} hook to interpret relative paths in an
 * environment-specific fashion, and/or {@link #getResourcePatternResolver}
 * for extended pattern resolution.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @see #getConfigResources
 * @see #getConfigLocations
 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableConfigApplicationContext {

	private boolean validating = true;


	/**
	 * Create a new AbstractXmlApplicationContext with no parent.
	 */
	public AbstractXmlApplicationContext() {
	}

	/**
	 * Create a new AbstractXmlApplicationContext with the given parent context.
	 * @param parent the parent context
	 */
	public AbstractXmlApplicationContext(@Nullable ApplicationContext parent) {
		super(parent);
	}


	/**
	 * Set whether to use XML validation. Default is {@code true}.
	 */
	public void setValidating(boolean validating) {
		this.validating = validating;
	}


	/**
	 * Loads the bean definitions via an XmlBeanDefinitionReader.
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 * @see #initBeanDefinitionReader
	 * @see #loadBeanDefinitions
	 * 通过XmlBeanDefinitionReader加载bean的定义
	 */
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		//为给定的 BeanFactory 创建新的XmlBeanDefinitionReader，用于加载解析配置文件
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		/*配置XmlBeanDefinitionReader*/
		//设置环境变量对象
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		//设置资源加载器resourceLoader，用于加载XML文件到内存中成为Resource。这里设置的是父类AbstractBeanDefinitionReader的属性，
		//将其设置为当前ClassPathXmlApplicationContext容器对象，因为容器也实现了ResourceLoader接口
		beanDefinitionReader.setResourceLoader(this);
		//设置SAX 实体解析器，用于分析Document
		// <3> 这个适用于解析xml dtd 和 xsd，最终使用的还是 java EntityResolver
		// 解析 xml 全靠它，xml 转换至 document 对象后，spring 才能创建 BeanDefinitions
		// (不太重要，可以略过，有兴趣的可以了解)
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		//初始化beanDefinitionReader，允许子类提供自定义初始化方法
		initBeanDefinitionReader(beanDefinitionReader);
		// 实际加载 bean 定义的方法，核心方法
		// <5> 加载 bean，从 xml 读取配置信息
		// 编写NamespaceHandler和BeanDefinitionParser完成解析工作
		loadBeanDefinitions(beanDefinitionReader);
	}

	/**
	 * Initialize the bean definition reader used for loading the bean
	 * definitions of this context. Default implementation is empty.
	 * <p>Can be overridden in subclasses, e.g. for turning off XML validation
	 * or using a different XmlBeanDefinitionParser implementation.
	 * @param reader the bean definition reader used by this context
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader#setDocumentReaderClass
	 */
	protected void initBeanDefinitionReader(XmlBeanDefinitionReader reader) {
		reader.setValidating(this.validating);
	}

	/**
	 * Load the bean definitions with the given XmlBeanDefinitionReader.
	 * <p>The lifecycle of the bean factory is handled by the {@link #refreshBeanFactory}
	 * method; hence this method is just supposed to load and/or register bean definitions.
	 * @param reader the XmlBeanDefinitionReader to use
	 * @throws BeansException in case of bean registration errors
	 * @throws IOException if the required XML document isn't found
	 * @see #refreshBeanFactory
	 * @see #getConfigLocations
	 * @see #getResources
	 * @see #getResourcePatternResolver
	 *
	 * 1.首先尝试获取配置文件的Resource数组configResources
	 * 2.如果configResources为null，那么获取配置文件路径字符串数组configLocations，在此前最外层的setConfigLocations方法中已经初始化了。非web容器第一次进来默认就是走的这个逻辑。
	 */
	protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		//获取配置文件的Resource数组，该属性在ClassPathXmlApplicationContext中，默认为null
		//在前面的setConfigLocations方法中解析的是configLocations配置文件路径字符串数组，注意区分

		// tips：
		// getConfigResources  和 getConfigLocations 区别是 new ClassPathXmlApplication(xxx.xml) 时候，
		// 提供了多个构造器，一个是通过 xml，另一个可以指定 xx.class 对象，为什么呢？
		// 这里和 ClassPathResource 有关，ClassPathResource 里面有可以指定一个 class 对象，和 classLoader，用于加载资源
		// 优先使用 class，没有才用 classLoader

		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			//调用reader自己的loadBeanDefinitions方法，加载bean 的定义
			reader.loadBeanDefinitions(configResources);
		}
		//获取配置文件路径数组，在此前最外层的setConfigLocations方法中已经初始化了
		//非web容器第一次进来默认就是走的这个逻辑
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			//调用reader自己的loadBeanDefinitions方法，加载bean 的定义
			//内部会从资源路径字符串处加载资源成为Resource，从还是会调用上面的loadBeanDefinitions方法
			reader.loadBeanDefinitions(configLocations);
		}
	}

	/**
	 * Return an array of Resource objects, referring to the XML bean definition
	 * files that this context should be built with.
	 * <p>The default implementation returns {@code null}. Subclasses can override
	 * this to provide pre-built Resource objects rather than location Strings.
	 * @return an array of Resource objects, or {@code null} if none
	 * @see #getConfigLocations()
	 */
	@Nullable
	protected Resource[] getConfigResources() {
		return null;
	}

}
