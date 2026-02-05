package org.jenkinsci.plugins.ParameterizedRemoteTrigger.headers;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.ParameterizedRemoteTrigger.BuildContext;
import org.jenkinsci.plugins.ParameterizedRemoteTrigger.utils.TokenMacroUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for a list of custom HTTP headers.
 */
public class CustomHeaders extends AbstractDescribableImpl<CustomHeaders> implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private List<CustomHeader> headers;

    /**
     * Default constructor for deserialization.
     */
    public CustomHeaders() {
        this.headers = new ArrayList<>();
    }

    @DataBoundConstructor
    public CustomHeaders(List<CustomHeader> headers) {
        this.headers = headers != null ? new ArrayList<>(headers) : new ArrayList<>();
    }

    public List<CustomHeader> getHeaders() {
        return headers;
    }

    @DataBoundSetter
    public void setHeaders(List<CustomHeader> headers) {
        this.headers = headers != null ? new ArrayList<>(headers) : new ArrayList<>();
    }

    /**
     * Converts the headers to a map with token macro expansion applied.
     *
     * @param context The build context for token macro expansion
     * @return Map of header names to values with macros expanded
     */
    public Map<String, String> getHeadersMap(BuildContext context) {
        Map<String, String> headersMap = new LinkedHashMap<>();

        if (headers == null || headers.isEmpty()) {
            return headersMap;
        }

        for (CustomHeader header : headers) {
            if (header == null) {
                continue;
            }

            String name = header.getName();
            String value = header.getValue();

            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            try {
                // Apply token macro expansion to both name and value
                String expandedName = TokenMacroUtils.applyTokenMacroReplacements(name, context);
                String expandedValue = TokenMacroUtils.applyTokenMacroReplacements(value, context);

                headersMap.put(expandedName, expandedValue);
            } catch (Exception e) {
                // If token expansion fails, use the original values
                headersMap.put(name, value);
            }
        }

        return headersMap;
    }

    @Override
    public CustomHeaders clone() {
        try {
            CustomHeaders cloned = (CustomHeaders) super.clone();
            if (this.headers != null) {
                cloned.headers = new ArrayList<>();
                for (CustomHeader header : this.headers) {
                    cloned.headers.add(header != null ? header.clone() : null);
                }
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Failed to clone CustomHeaders", e);
        }
    }

    @Extension
    @Symbol("CustomHeaders")
    public static class DescriptorImpl extends Descriptor<CustomHeaders> {
        @Override
        public String getDisplayName() {
            return "Custom Headers";
        }
    }
}
