package org.linlinjava.litemall.db.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class UserBalanceHistory {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    public static final Boolean IS_DELETED = Deleted.IS_DELETED.value();

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    public static final Boolean NOT_DELETED = Deleted.NOT_DELETED.value();

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.user_id
     *
     * @mbg.generated
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.change_sum
     *
     * @mbg.generated
     */
    private BigDecimal changeSum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.user_balance
     *
     * @mbg.generated
     */
    private BigDecimal userBalance;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.change_type
     *
     * @mbg.generated
     */
    private Byte changeType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.relation_id
     *
     * @mbg.generated
     */
    private Integer relationId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.add_time
     *
     * @mbg.generated
     */
    private LocalDateTime addTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.update_time
     *
     * @mbg.generated
     */
    private LocalDateTime updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_balance_history.deleted
     *
     * @mbg.generated
     */
    private Boolean deleted;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.id
     *
     * @return the value of user_balance_history.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.id
     *
     * @param id the value for user_balance_history.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.user_id
     *
     * @return the value of user_balance_history.user_id
     *
     * @mbg.generated
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.user_id
     *
     * @param userId the value for user_balance_history.user_id
     *
     * @mbg.generated
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.change_sum
     *
     * @return the value of user_balance_history.change_sum
     *
     * @mbg.generated
     */
    public BigDecimal getChangeSum() {
        return changeSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.change_sum
     *
     * @param changeSum the value for user_balance_history.change_sum
     *
     * @mbg.generated
     */
    public void setChangeSum(BigDecimal changeSum) {
        this.changeSum = changeSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.user_balance
     *
     * @return the value of user_balance_history.user_balance
     *
     * @mbg.generated
     */
    public BigDecimal getUserBalance() {
        return userBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.user_balance
     *
     * @param userBalance the value for user_balance_history.user_balance
     *
     * @mbg.generated
     */
    public void setUserBalance(BigDecimal userBalance) {
        this.userBalance = userBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.change_type
     *
     * @return the value of user_balance_history.change_type
     *
     * @mbg.generated
     */
    public Byte getChangeType() {
        return changeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.change_type
     *
     * @param changeType the value for user_balance_history.change_type
     *
     * @mbg.generated
     */
    public void setChangeType(Byte changeType) {
        this.changeType = changeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.relation_id
     *
     * @return the value of user_balance_history.relation_id
     *
     * @mbg.generated
     */
    public Integer getRelationId() {
        return relationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.relation_id
     *
     * @param relationId the value for user_balance_history.relation_id
     *
     * @mbg.generated
     */
    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.add_time
     *
     * @return the value of user_balance_history.add_time
     *
     * @mbg.generated
     */
    public LocalDateTime getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.add_time
     *
     * @param addTime the value for user_balance_history.add_time
     *
     * @mbg.generated
     */
    public void setAddTime(LocalDateTime addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.update_time
     *
     * @return the value of user_balance_history.update_time
     *
     * @mbg.generated
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.update_time
     *
     * @param updateTime the value for user_balance_history.update_time
     *
     * @mbg.generated
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    public void andLogicalDeleted(boolean deleted) {
        setDeleted(deleted ? Deleted.IS_DELETED.value() : Deleted.NOT_DELETED.value());
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_balance_history.deleted
     *
     * @return the value of user_balance_history.deleted
     *
     * @mbg.generated
     */
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_balance_history.deleted
     *
     * @param deleted the value for user_balance_history.deleted
     *
     * @mbg.generated
     */
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", IS_DELETED=").append(IS_DELETED);
        sb.append(", NOT_DELETED=").append(NOT_DELETED);
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", changeSum=").append(changeSum);
        sb.append(", userBalance=").append(userBalance);
        sb.append(", changeType=").append(changeType);
        sb.append(", relationId=").append(relationId);
        sb.append(", addTime=").append(addTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", deleted=").append(deleted);
        sb.append("]");
        return sb.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_balance_history
     *
     * @mbg.generated
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
        UserBalanceHistory other = (UserBalanceHistory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getChangeSum() == null ? other.getChangeSum() == null : this.getChangeSum().equals(other.getChangeSum()))
            && (this.getUserBalance() == null ? other.getUserBalance() == null : this.getUserBalance().equals(other.getUserBalance()))
            && (this.getChangeType() == null ? other.getChangeType() == null : this.getChangeType().equals(other.getChangeType()))
            && (this.getRelationId() == null ? other.getRelationId() == null : this.getRelationId().equals(other.getRelationId()))
            && (this.getAddTime() == null ? other.getAddTime() == null : this.getAddTime().equals(other.getAddTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getChangeSum() == null) ? 0 : getChangeSum().hashCode());
        result = prime * result + ((getUserBalance() == null) ? 0 : getUserBalance().hashCode());
        result = prime * result + ((getChangeType() == null) ? 0 : getChangeType().hashCode());
        result = prime * result + ((getRelationId() == null) ? 0 : getRelationId().hashCode());
        result = prime * result + ((getAddTime() == null) ? 0 : getAddTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    public enum Deleted {
        NOT_DELETED(new Boolean("0"), "未删除"),
        IS_DELETED(new Boolean("1"), "已删除");

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final Boolean value;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final String name;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        Deleted(Boolean value, String name) {
            this.value = value;
            this.name = name;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public Boolean getValue() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public Boolean value() {
            return this.value;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * This enum was generated by MyBatis Generator.
     * This enum corresponds to the database table user_balance_history
     *
     * @mbg.generated
     */
    public enum Column {
        id("id", "id", "INTEGER", false),
        userId("user_id", "userId", "INTEGER", false),
        changeSum("change_sum", "changeSum", "DECIMAL", false),
        userBalance("user_balance", "userBalance", "DECIMAL", false),
        changeType("change_type", "changeType", "TINYINT", false),
        relationId("relation_id", "relationId", "INTEGER", false),
        addTime("add_time", "addTime", "TIMESTAMP", false),
        updateTime("update_time", "updateTime", "TIMESTAMP", false),
        deleted("deleted", "deleted", "BIT", false);

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private static final String BEGINNING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private static final String ENDING_DELIMITER = "`";

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final String column;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final boolean isColumnNameDelimited;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final String javaProperty;

        /**
         * This field was generated by MyBatis Generator.
         * This field corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        private final String jdbcType;

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String value() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getValue() {
            return this.column;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getJavaProperty() {
            return this.javaProperty;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getJdbcType() {
            return this.jdbcType;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        Column(String column, String javaProperty, String jdbcType, boolean isColumnNameDelimited) {
            this.column = column;
            this.javaProperty = javaProperty;
            this.jdbcType = jdbcType;
            this.isColumnNameDelimited = isColumnNameDelimited;
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String desc() {
            return this.getEscapedColumnName() + " DESC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String asc() {
            return this.getEscapedColumnName() + " ASC";
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public static Column[] excludes(Column ... excludes) {
            ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));
            if (excludes != null && excludes.length > 0) {
                columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));
            }
            return columns.toArray(new Column[]{});
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getEscapedColumnName() {
            if (this.isColumnNameDelimited) {
                return new StringBuilder().append(BEGINNING_DELIMITER).append(this.column).append(ENDING_DELIMITER).toString();
            } else {
                return this.column;
            }
        }

        /**
         * This method was generated by MyBatis Generator.
         * This method corresponds to the database table user_balance_history
         *
         * @mbg.generated
         */
        public String getAliasedEscapedColumnName() {
            return this.getEscapedColumnName();
        }
    }
}