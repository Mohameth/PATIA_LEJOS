/*
 * Copyright (c) 2010 by Damien Pellier <Damien.Pellier@imag.fr>.
 *
 * This file is part of PDDL4J library.
 *
 * PDDL4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PDDL4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PDDL4J.  If not, see <http://www.gnu.org/licenses/>
 */

package fr.uga.pddl4j.parser;

import java.io.File;
import java.io.Serializable;

/**
 * This class implements an error message that may generated by the parser.
 *
 * @author D. Pellier
 * @version 1.0 - 28.01.10
 */
public class Message implements Serializable, Comparable<Message> {

    /**
     * The serial version id of the class.
     */
    private static final long serialVersionUID = -6769894590368065516L;

    /**
     * This enumeration defines the type of message log in the error manager.
     */
    public enum Type {
        /**
         * The parser error message.
         */
        PARSER_ERROR,
        /**
         * The parser warning message.
         */
        PARSER_WARNING,
        /**
         * The lexical error message.
         */
        LEXICAL_ERROR,
    }

    /**
     * The type of message.
     */
    private Type type;

    /**
     * The line concerned by the message.
     */
    private int line;

    /**
     * The column concerned by the message.
     */
    private int column;

    /**
     * The file concerned by the message.
     */
    private File file;

    /**
     * The content of the message.
     */
    private String content;

    /**
     * Creates a message with a specified type, line, column, file and content.
     *
     * @param type    the type of message.
     * @param line    the line of the file concerned by this message.
     * @param column  the column of the file concerned by this message.
     * @param file    the file concerned by this message.
     * @param content the content of the message.
     */
    public Message(final Type type, final int line, final int column, final File file, final String content) {
        if (type == null || file == null) {
            throw new NullPointerException();
        }
        this.type = type;
        this.line = line;
        this.column = column;
        this.file = file;
        this.content = content;
    }

    /**
     * Returns the type of the message.
     *
     * @return the type of the message.
     */
    public final Type getType() {
        return this.type;
    }

    /**
     * Sets the type of the message.
     *
     * @param type the type of message to set.
     */
    public final void setType(final Type type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = type;
    }

    /**
     * Returns the line of the file concerned by this message.
     *
     * @return the line of the file concerned by this message.
     */
    public final int getLine() {
        return this.line;
    }

    /**
     * Sets the line of the file concerned by this message.
     *
     * @param line the line to set.
     */
    public final void setLine(final int line) {
        this.line = line;
    }

    /**
     * Returns the column of the file concerned by this message.
     *
     * @return the column of the file concerned by this message.
     */
    public final int getColumn() {
        return this.column;
    }

    /**
     * Sets the column of the file concerned by this message.
     *
     * @param column the column to set.
     */
    public final void setColumn(final int column) {
        this.column = column;
    }

    /**
     * Returns the file concerned by this message.
     *
     * @return the file concerned by this message.
     */
    public final File getFile() {
        return this.file;
    }

    /**
     * Sets the file concerned by this message.
     *
     * @param file the file to set.
     */
    public final void setFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.file = file;
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message.
     */
    public final String getContent() {
        return this.content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content the content to set.
     */
    public final void setContent(final String content) {
        this.content = content;
    }

    /**
     * Returns if an object is equals to this message. The method returns <code>true</code> if the
     * object is not <code>null</code> and is an instance of the class <code>Message</code> and
     * it has the same line, column and file.
     *
     * @param obj the other object.
     * @return <code>true</code>if an object is equals to this message; <code>false</code>.
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            final Message other = (Message) obj;
            return this.line == other.line
                && this.column == other.column
                && this.file.equals(other.file);
        }
        return false;
    }

    /**
     * Returns the hash code value of this message.
     *
     * @return the hash code value of this message.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.line + this.column + this.file.hashCode();
    }

    /**
     * Compare this message to another message. The order used to compare two messages is the
     * following:
     * <ul>
     * <li>1. the file concerned according to the alphabetical order.</li>
     * <li>2. the line concerned.</li>
     * <li>3. the line concerned.</li>
     * <li>4. the type of the message.</li>
     * </ul>
     *
     * @param other the other message.
     * @return a negative number if this message comes before the other message, 0 is the message
     *          are equals or a positive number if this message comes after the other one according
     *          to the defined order.
     * @see java.lang.Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(final Message other) {
        int value = this.file.compareTo(other.file);
        if (value == 0) {
            value = this.line - other.line;
            if (value == 0) {
                value = this.column - other.column;
                if (value == 0) {
                    value = this.type.compareTo(other.type);
                }
            }
        }
        return value;
    }

    /**
     * Returns a string representation of this message.
     *
     * @return a string representation of this message.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        if (this.type == Type.LEXICAL_ERROR) {
            str.append("Lexical error");
        } else if (this.type == Type.PARSER_ERROR) {
            str.append("Parser error");
        } else {
            str.append("Parser warning");
        }
        str.append(" at line ").append(this.line).append(", column ").append(this.column)
            .append(", file (").append(this.file).append(")")
            .append(" : ").append(this.content).append('\n');
        return str.toString();


    }

}
