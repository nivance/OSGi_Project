/*
 * Copyright 2014 the original author or authors.
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
package nivance.jpa.cassandra.prepare.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to identify fields by a specific tag. Tagged fields do not have dependency for the field name and it is a
 * refactoring friendly approach.
 * 
 * Example:
 * 
 * <code>
 * class Profile { @Tag(USER_NAME) String userName; String firstName; String lastName; }
 * cassandraTemplate.save(profile).taggedFields(USER_NAME).execute(); 
 * </code>
 * 
 * Will update only userName field.
 * 
 * @author Alex Shvid
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.ANNOTATION_TYPE })
public @interface Tag {

	int value();

}
