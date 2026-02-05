package org.jenkinsci.plugins.ParameterizedRemoteTrigger.headers;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.Secret;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;

/**
 * Represents a single custom HTTP header with optional encryption support.
 */
public class CustomHeader extends AbstractDescribableImpl<CustomHeader> implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Secret value;
    private boolean isSecret;

    /**
     * Default constructor for deserialization.
     */
    public CustomHeader() {
        this.name = "";
        this.value = Secret.fromString("");
        this.isSecret = false;
    }

    /**
     * Constructor for creating a custom header.
     */
    @DataBoundConstructor
    public CustomHeader(String name, String value) {
        this.name = name != null ? name : "";
        this.value = Secret.fromString(value != null ? value : "");
        this.isSecret = false;
    }

    public String getName() {
        return name;
    }

    @DataBoundSetter
    public void setName(String name) {
        this.name = name != null ? name : "";
    }

    /**
     * Returns the decrypted header value.
     */
    public String getValue() {
        return value != null ? Secret.toString(value) : "";
    }

    @DataBoundSetter
    public void setValue(String value) {
        this.value = Secret.fromString(value != null ? value : "");
    }

    public boolean isSecret() {
        return isSecret;
    }

    @DataBoundSetter
    public void setIsSecret(boolean isSecret) {
        this.isSecret = isSecret;
    }

    @Override
    public CustomHeader clone() {
        try {
            CustomHeader cloned = (CustomHeader) super.clone();
            cloned.name = this.name;
            cloned.value = this.value;
            cloned.isSecret = this.isSecret;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone CustomHeader", e);
        }
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<CustomHeader> {
        @Override
        public String getDisplayName() {
            return "Custom Header";
        }
    }
}
