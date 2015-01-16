package nivance.simplecass.cassandra.annotation;
public enum KeyPart {

	/**
	 * Used for a column that is part of the partition key.
	 */
	PARTITION,

	/**
	 * Used for a column that is clustering key.
	 */
	CLUSTERING
}