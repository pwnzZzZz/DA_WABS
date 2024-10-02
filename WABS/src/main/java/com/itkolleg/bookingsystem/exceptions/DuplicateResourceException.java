package com.itkolleg.bookingsystem.exceptions;

/**
 * This class represents a custom exception which is thrown when trying to create a resource that already exists.
 *
 * @author Sonja Lechner
 * @version 1.0
 * @since 2023-05-24
 */
public class DuplicateResourceException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    /**
     * Constructs a new exception with the specified resource name, field name and field value.
     *
     * @param resourceName the name of the resource that duplicates an existing one
     * @param fieldName    the name of the field that caused the duplication
     * @param fieldValue   the value of the field that caused the duplication
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s : '%s' already exists", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Get the resource name associated with this exception.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Get the field name associated with this exception.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Get the field value associated with this exception.
     *
     * @return the field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
