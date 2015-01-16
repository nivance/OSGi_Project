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
package nivance.jpa.cassandra.prepare.generator;

import static nivance.jpa.cassandra.prepare.util.CqlStringUtils.noNull;
import nivance.jpa.cassandra.prepare.option.TableOption;
import nivance.jpa.cassandra.prepare.spec.AddColumnSpecification;
import nivance.jpa.cassandra.prepare.spec.AlterColumnSpecification;
import nivance.jpa.cassandra.prepare.spec.AlterTableSpecification;
import nivance.jpa.cassandra.prepare.spec.ColumnChangeSpecification;
import nivance.jpa.cassandra.prepare.spec.DropColumnSpecification;

/**
 * CQL generator for generating <code>ALTER TABLE</code> statements.
 * 
 * @author Matthew T. Adams
 */
public class AlterTableCqlGenerator extends WithOptionsCqlGenerator<TableOption, AlterTableSpecification> {

	public AlterTableCqlGenerator(AlterTableSpecification specification) {
		super(specification);
	}

	public StringBuilder toCql(StringBuilder cql) {
		cql = preambleCql(cql);
		cql = changesCql(cql);
		return optionsCql(cql, null).append(";");
	}

	protected StringBuilder preambleCql(StringBuilder cql) {
		return noNull(cql).append("ALTER TABLE ").append(spec().getNameAsIdentifier()).append(" ");
	}

	protected StringBuilder changesCql(StringBuilder cql) {
		cql = noNull(cql);

		boolean first = true;
		for (ColumnChangeSpecification change : spec().getChanges()) {
			if (first) {
				first = false;
			} else {
				cql.append(" ");
			}
			getCqlGeneratorFor(change).toCql(cql);
		}

		return cql;
	}

	protected ColumnChangeCqlGenerator<?> getCqlGeneratorFor(ColumnChangeSpecification change) {
		if (change instanceof AddColumnSpecification) {
			return new AddColumnCqlGenerator((AddColumnSpecification) change);
		}
		if (change instanceof DropColumnSpecification) {
			return new DropColumnCqlGenerator((DropColumnSpecification) change);
		}
		if (change instanceof AlterColumnSpecification) {
			return new AlterColumnCqlGenerator((AlterColumnSpecification) change);
		}
		throw new IllegalArgumentException("unknown ColumnChangeSpecification type: " + change.getClass().getName());
	}

}
