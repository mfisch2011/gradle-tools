/**
 * 
 */
package com.github.mfisch2011.gradle.test;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * TODO:documentation...
 *
 */
public @interface GradleTest {

	/**
	 * TODO:documentation...
	 * @return
	 */
	public String[] args() default {};
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public String build() default "";
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public String settings() default "";
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	public String properties() default "";
	
	/**
	 * TODO:documentation...
	 * @return
	 */
	//TODO:public String resources() default "";
}
