package nivance.jpa.mysql.entity;

import org.apache.avro.reflect.Nullable;

public class AdminOperatorKey {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column T_ADMIN_OPERATOR.USERNAME
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    @Nullable
    private String username;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column T_ADMIN_OPERATOR.USERNAME
     *
     * @return the value of T_ADMIN_OPERATOR.USERNAME
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column T_ADMIN_OPERATOR.USERNAME
     *
     * @param username the value for T_ADMIN_OPERATOR.USERNAME
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        AdminOperatorKey other = (AdminOperatorKey) that;
        return (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table T_ADMIN_OPERATOR
     *
     * @mbggenerated Mon May 19 21:35:25 CST 2014
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", username=").append(username);
        sb.append("]");
        return sb.toString();
    }
}