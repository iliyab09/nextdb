/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;
/**
 * Error represents errors return when executing database operations
 */
public class Error {

    /**
     * a numeric error code representing a classification of error type.
     */
    private int code = 0;
    /**
     * a context-specific error message string
     */
    private String message = "";
    /**
     * An additional field, named "causedBy" will be populated when the error
     * applies to a specific column, parameter, or relationship. The causedBy
     * column is usefulin situations like form validation, where the application
     * needs to know, for example, that there was a problem with a particular field.
     */
    private String causedBy = "";

    public Error() {
    }

    public Error(String message) {
        this.message = message;
    }

    public String toString() {
        return this.getMessage();
    }

    /**
     * a numeric error code representing a classification of error type.
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * a context-specific error message string
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * An additional field, named "causedBy" will be populated when the error
     * applies to a specific column, parameter, or relationship. The causedBy
     * column is usefulin situations like form validation, where the application
     * needs to know, for example, that there was a problem with a particular field.
     * @return the causedBy
     */
    public String getCausedBy() {
        return causedBy;
    }

    /**
     * @param causedBy the causedBy to set
     */
    public void setCausedBy(String causedBy) {
        this.causedBy = causedBy;
    }
}
