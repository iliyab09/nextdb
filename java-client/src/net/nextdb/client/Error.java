/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

public class Error{
    /**
	 * a numeric error code representing a classification of error type.
	 */
    private int code=0;
    /**
	 * a context-specific error message string
	 */
    private String message="";
    /**
	 * An additional field, named "causedBy" will be populated when the error
	 * applies to a specific column, parameter, or relationship. The causedBy
	 * column is usefulin situations like form validation, where the application
	 * needs to know, for example, that there was a problem with a particular field.
	 */
    private String causedBy="";

    public Error() {
    }

    public Error(String message) {
	  this.message=message;
    }

    public String getCausedBy() {
	  return causedBy;
    }

    public void setCausedBy(String causedBy) {
	  this.causedBy = causedBy;
    }

    public int getCode() {
	  return code;
    }

    public void setCode(int code) {
	  this.code = code;
    }

    public String getMessage() {
	  return message;
    }

    public void setMessage(String message) {
	  this.message = message;
    }
    public String toString(){
	  return this.getMessage();
    }
}
