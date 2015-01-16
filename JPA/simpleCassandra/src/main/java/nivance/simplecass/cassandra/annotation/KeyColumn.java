package nivance.simplecass.cassandra.annotation;
/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.cassandra.core.Ordering;

/**
 * Identifies partition key in the Cassandra composite primary key class. Annotated column is the part of the Cassandra
 * Partition Key (former Row Id).
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface KeyColumn {

	/**
	 * Key part of the primary key.
	 * 
	 * @return
	 */

	KeyPart keyPart() default KeyPart.PARTITION;

	/**
	 * Order of the column in the primary key.
	 * 
	 * @return
	 */

	int ordinal() default 0;

	/**
	 * Ordering of the column in the table.
	 * 
	 * @return
	 */
	Ordering ordering() default Ordering.ASCENDING;

}
