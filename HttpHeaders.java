
package org.springframework.http;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

public class HttpHeaders implements MultiValueMap<String, String>, Serializable {
    private static final long serialVersionUID = -8578554704772377436L;
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPECT = "Expect";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LINK = "Link";
    public static final String LOCATION = "Location";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
    public static final String TE = "TE";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String[] DATE_FORMATS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy"};
    private static final Pattern ETAG_HEADER_VALUE_PATTERN = Pattern.compile("\\*|\\s*((W\\/)?(\"[^\"]*\"))\\s*,?");
    private static TimeZone GMT = TimeZone.getTimeZone("GMT");
    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        Assert.notNull(headers, "\'headers\' must not be null");
        if(readOnly) {
            LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
            Iterator var4 = headers.entrySet().iterator();

            while(var4.hasNext()) {
                Entry entry = (Entry)var4.next();
                List values = Collections.unmodifiableList((List)entry.getValue());
                map.put(entry.getKey(), values);
            }

            this.headers = Collections.unmodifiableMap(map);
        } else {
            this.headers = headers;
        }

    }

    public void setAccept(List<MediaType> acceptableMediaTypes) {
        this.set("Accept", MediaType.toString(acceptableMediaTypes));
    }

    public List<MediaType> getAccept() {
        return MediaType.parseMediaTypes(this.get("Accept"));
    }

    public void setAccessControlAllowCredentials(boolean allowCredentials) {
        this.set("Access-Control-Allow-Credentials", Boolean.toString(allowCredentials));
    }

    public boolean getAccessControlAllowCredentials() {
        return Boolean.parseBoolean(this.getFirst("Access-Control-Allow-Credentials"));
    }

    public void setAccessControlAllowHeaders(List<String> allowedHeaders) {
        this.set("Access-Control-Allow-Headers", this.toCommaDelimitedString(allowedHeaders));
    }

    public List<String> getAccessControlAllowHeaders() {
        return this.getValuesAsList("Access-Control-Allow-Headers");
    }

    public void setAccessControlAllowMethods(List<HttpMethod> allowedMethods) {
        this.set("Access-Control-Allow-Methods", StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public List<HttpMethod> getAccessControlAllowMethods() {
        ArrayList result = new ArrayList();
        String value = this.getFirst("Access-Control-Allow-Methods");
        if(value != null) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            String[] var4 = tokens;
            int var5 = tokens.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String token = var4[var6];
                HttpMethod resolved = HttpMethod.resolve(token);
                if(resolved != null) {
                    result.add(resolved);
                }
            }
        }

        return result;
    }

    public void setAccessControlAllowOrigin(String allowedOrigin) {
        this.set("Access-Control-Allow-Origin", allowedOrigin);
    }

    public String getAccessControlAllowOrigin() {
        return this.getFieldValues("Access-Control-Allow-Origin");
    }

    public void setAccessControlExposeHeaders(List<String> exposedHeaders) {
        this.set("Access-Control-Expose-Headers", this.toCommaDelimitedString(exposedHeaders));
    }

    public List<String> getAccessControlExposeHeaders() {
        return this.getValuesAsList("Access-Control-Expose-Headers");
    }

    public void setAccessControlMaxAge(long maxAge) {
        this.set("Access-Control-Max-Age", Long.toString(maxAge));
    }

    public long getAccessControlMaxAge() {
        String value = this.getFirst("Access-Control-Max-Age");
        return value != null?Long.parseLong(value):-1L;
    }

    public void setAccessControlRequestHeaders(List<String> requestHeaders) {
        this.set("Access-Control-Request-Headers", this.toCommaDelimitedString(requestHeaders));
    }

    public List<String> getAccessControlRequestHeaders() {
        return this.getValuesAsList("Access-Control-Request-Headers");
    }

    public void setAccessControlRequestMethod(HttpMethod requestMethod) {
        this.set("Access-Control-Request-Method", requestMethod.name());
    }

    public HttpMethod getAccessControlRequestMethod() {
        return HttpMethod.resolve(this.getFirst("Access-Control-Request-Method"));
    }

    public void setAcceptCharset(List<Charset> acceptableCharsets) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = acceptableCharsets.iterator();

        while(iterator.hasNext()) {
            Charset charset = (Charset)iterator.next();
            builder.append(charset.name().toLowerCase(Locale.ENGLISH));
            if(iterator.hasNext()) {
                builder.append(", ");
            }
        }

        this.set("Accept-Charset", builder.toString());
    }

    public List<Charset> getAcceptCharset() {
        String value = this.getFirst("Accept-Charset");
        if(value != null) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            ArrayList result = new ArrayList(tokens.length);
            String[] var4 = tokens;
            int var5 = tokens.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String token = var4[var6];
                int paramIdx = token.indexOf(59);
                String charsetName;
                if(paramIdx == -1) {
                    charsetName = token;
                } else {
                    charsetName = token.substring(0, paramIdx);
                }

                if(!charsetName.equals("*")) {
                    result.add(Charset.forName(charsetName));
                }
            }

            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public void setAllow(Set<HttpMethod> allowedMethods) {
        this.set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public Set<HttpMethod> getAllow() {
        String value = this.getFirst("Allow");
        if(!StringUtils.isEmpty(value)) {
            String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
            ArrayList result = new ArrayList(tokens.length);
            String[] var4 = tokens;
            int var5 = tokens.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String token = var4[var6];
                HttpMethod resolved = HttpMethod.resolve(token);
                if(resolved != null) {
                    result.add(resolved);
                }
            }

            return EnumSet.copyOf(result);
        } else {
            return EnumSet.noneOf(HttpMethod.class);
        }
    }

    public void setCacheControl(String cacheControl) {
        this.set("Cache-Control", cacheControl);
    }

    public String getCacheControl() {
        return this.getFieldValues("Cache-Control");
    }

    public void setConnection(String connection) {
        this.set("Connection", connection);
    }

    public void setConnection(List<String> connection) {
        this.set("Connection", this.toCommaDelimitedString(connection));
    }

    public List<String> getConnection() {
        return this.getValuesAsList("Connection");
    }

    public void setContentDispositionFormData(String name, String filename) {
        this.setContentDispositionFormData(name, filename, (Charset)null);
    }

    public void setContentDispositionFormData(String name, String filename, Charset charset) {
        Assert.notNull(name, "\'name\' must not be null");
        StringBuilder builder = new StringBuilder("form-data; name=\"");
        builder.append(name).append('\"');
        if(filename != null) {
            if(charset != null && !charset.name().equals("US-ASCII")) {
                builder.append("; filename*=");
                builder.append(encodeHeaderFieldParam(filename, charset));
            } else {
                builder.append("; filename=\"");
                builder.append(filename).append('\"');
            }
        }

        this.set("Content-Disposition", builder.toString());
    }

    public void setContentLength(long contentLength) {
        this.set("Content-Length", Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = this.getFirst("Content-Length");
        return value != null?Long.parseLong(value):-1L;
    }

    public void setContentType(MediaType mediaType) {
        Assert.isTrue(!mediaType.isWildcardType(), "\'Content-Type\' cannot contain wildcard type \'*\'");
        Assert.isTrue(!mediaType.isWildcardSubtype(), "\'Content-Type\' cannot contain wildcard subtype \'*\'");
        this.set("Content-Type", mediaType.toString());
    }

    public MediaType getContentType() {
        String value = this.getFirst("Content-Type");
        return StringUtils.hasLength(value)?MediaType.parseMediaType(value):null;
    }

    public void setDate(long date) {
        this.setDate("Date", date);
    }

    public long getDate() {
        return this.getFirstDate("Date");
    }

    public void setETag(String eTag) {
        if(eTag != null) {
            Assert.isTrue(eTag.startsWith("\"") || eTag.startsWith("W/"), "Invalid eTag, does not start with W/ or \"");
            Assert.isTrue(eTag.endsWith("\""), "Invalid eTag, does not end with \"");
        }

        this.set("ETag", eTag);
    }

    public String getETag() {
        return this.getFirst("ETag");
    }

    public void setExpires(long expires) {
        this.setDate("Expires", expires);
    }

    public long getExpires() {
        return this.getFirstDate("Expires", false);
    }

    public void setIfMatch(String ifMatch) {
        this.set("If-Match", ifMatch);
    }

    public void setIfMatch(List<String> ifMatchList) {
        this.set("If-Match", this.toCommaDelimitedString(ifMatchList));
    }

    public List<String> getIfMatch() {
        return this.getETagValuesAsList("If-Match");
    }

    public void setIfModifiedSince(long ifModifiedSince) {
        this.setDate("If-Modified-Since", ifModifiedSince);
    }

    public long getIfModifiedSince() {
        return this.getFirstDate("If-Modified-Since", false);
    }

    public void setIfNoneMatch(String ifNoneMatch) {
        this.set("If-None-Match", ifNoneMatch);
    }

    public void setIfNoneMatch(List<String> ifNoneMatchList) {
        this.set("If-None-Match", this.toCommaDelimitedString(ifNoneMatchList));
    }

    public List<String> getIfNoneMatch() {
        return this.getETagValuesAsList("If-None-Match");
    }

    public void setIfUnmodifiedSince(long ifUnmodifiedSince) {
        this.setDate("If-Unmodified-Since", ifUnmodifiedSince);
    }

    public long getIfUnmodifiedSince() {
        return this.getFirstDate("If-Unmodified-Since", false);
    }

    public void setLastModified(long lastModified) {
        this.setDate("Last-Modified", lastModified);
    }

    public long getLastModified() {
        return this.getFirstDate("Last-Modified", false);
    }

    public void setLocation(URI location) {
        this.set("Location", location.toASCIIString());
    }

    public URI getLocation() {
        String value = this.getFirst("Location");
        return value != null?URI.create(value):null;
    }

    public void setOrigin(String origin) {
        this.set("Origin", origin);
    }

    public String getOrigin() {
        return this.getFirst("Origin");
    }

    public void setPragma(String pragma) {
        this.set("Pragma", pragma);
    }

    public String getPragma() {
        return this.getFirst("Pragma");
    }

    public void setRange(List<HttpRange> ranges) {
        String value = HttpRange.toString(ranges);
        this.set("Range", value);
    }

    public List<HttpRange> getRange() {
        String value = this.getFirst("Range");
        return HttpRange.parseRanges(value);
    }

    public void setUpgrade(String upgrade) {
        this.set("Upgrade", upgrade);
    }

    public String getUpgrade() {
        return this.getFirst("Upgrade");
    }

    public void setVary(List<String> requestHeaders) {
        this.set("Vary", this.toCommaDelimitedString(requestHeaders));
    }

    public List<String> getVary() {
        return this.getValuesAsList("Vary");
    }

    public void setDate(String headerName, long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
        dateFormat.setTimeZone(GMT);
        this.set(headerName, dateFormat.format(new Date(date)));
    }

    public long getFirstDate(String headerName) {
        return this.getFirstDate(headerName, true);
    }

    private long getFirstDate(String headerName, boolean rejectInvalid) {
        String headerValue = this.getFirst(headerName);
        if(headerValue == null) {
            return -1L;
        } else {
            if(headerValue.length() >= 3) {
                String[] var4 = DATE_FORMATS;
                int var5 = var4.length;
                int var6 = 0;

                while(var6 < var5) {
                    String dateFormat = var4[var6];
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                    simpleDateFormat.setTimeZone(GMT);

                    try {
                        return simpleDateFormat.parse(headerValue).getTime();
                    } catch (ParseException var10) {
                        ++var6;
                    }
                }
            }

            if(rejectInvalid) {
                throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
            } else {
                return -1L;
            }
        }
    }

    public List<String> getValuesAsList(String headerName) {
        List values = this.get(headerName);
        if(values == null) {
            return Collections.emptyList();
        } else {
            ArrayList result = new ArrayList();
            Iterator var4 = values.iterator();

            while(true) {
                String value;
                do {
                    if(!var4.hasNext()) {
                        return result;
                    }

                    value = (String)var4.next();
                } while(value == null);

                String[] tokens = StringUtils.tokenizeToStringArray(value, ",");
                String[] var7 = tokens;
                int var8 = tokens.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String token = var7[var9];
                    result.add(token);
                }
            }
        }
    }

    protected List<String> getETagValuesAsList(String headerName) {
        List values = this.get(headerName);
        if(values == null) {
            return Collections.emptyList();
        } else {
            ArrayList result = new ArrayList();
            Iterator var4 = values.iterator();

            String value;
            do {
                do {
                    if(!var4.hasNext()) {
                        return result;
                    }

                    value = (String)var4.next();
                } while(value == null);

                Matcher matcher = ETAG_HEADER_VALUE_PATTERN.matcher(value);

                while(matcher.find()) {
                    if("*".equals(matcher.group())) {
                        result.add(matcher.group());
                    } else {
                        result.add(matcher.group(1));
                    }
                }
            } while(!result.isEmpty());

            throw new IllegalArgumentException("Could not parse header \'" + headerName + "\' with value \'" + value + "\'");
        }
    }

    protected String getFieldValues(String headerName) {
        List headerValues = this.get(headerName);
        return headerValues != null?this.toCommaDelimitedString(headerValues):null;
    }

    protected String toCommaDelimitedString(List<String> headerValues) {
        StringBuilder builder = new StringBuilder();
        Iterator it = headerValues.iterator();

        while(it.hasNext()) {
            String val = (String)it.next();
            builder.append(val);
            if(it.hasNext()) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public String getFirst(String headerName) {
        List headerValues = (List)this.headers.get(headerName);
        return headerValues != null?(String)headerValues.get(0):null;
    }

    public void add(String headerName, String headerValue) {
        Object headerValues = (List)this.headers.get(headerName);
        if(headerValues == null) {
            headerValues = new LinkedList();
            this.headers.put(headerName, headerValues);
        }

        ((List)headerValues).add(headerValue);
    }

    public void set(String headerName, String headerValue) {
        LinkedList headerValues = new LinkedList();
        headerValues.add(headerValue);
        this.headers.put(headerName, headerValues);
    }

    public void setAll(Map<String, String> values) {
        Iterator var2 = values.entrySet().iterator();

        while(var2.hasNext()) {
            Entry entry = (Entry)var2.next();
            this.set((String)entry.getKey(), (String)entry.getValue());
        }

    }

    public Map<String, String> toSingleValueMap() {
        LinkedHashMap singleValueMap = new LinkedHashMap(this.headers.size());
        Iterator var2 = this.headers.entrySet().iterator();

        while(var2.hasNext()) {
            Entry entry = (Entry)var2.next();
            singleValueMap.put(entry.getKey(), ((List)entry.getValue()).get(0));
        }

        return singleValueMap;
    }

    public int size() {
        return this.headers.size();
    }

    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    public List<String> get(Object key) {
        return (List)this.headers.get(key);
    }

    public List<String> put(String key, List<String> value) {
        return (List)this.headers.put(key, value);
    }

    public List<String> remove(Object key) {
        return (List)this.headers.remove(key);
    }

    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    public void clear() {
        this.headers.clear();
    }

    public Set<String> keySet() {
        return this.headers.keySet();
    }

    public Collection<List<String>> values() {
        return this.headers.values();
    }

    public Set<Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    public boolean equals(Object other) {
        if(this == other) {
            return true;
        } else if(!(other instanceof HttpHeaders)) {
            return false;
        } else {
            HttpHeaders otherHeaders = (HttpHeaders)other;
            return this.headers.equals(otherHeaders.headers);
        }
    }

    public int hashCode() {
        return this.headers.hashCode();
    }

    public String toString() {
        return this.headers.toString();
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        return new HttpHeaders(headers, true);
    }

    static String encodeHeaderFieldParam(String input, Charset charset) {
        Assert.notNull(input, "Input String should not be null");
        Assert.notNull(charset, "Charset should not be null");
        if(charset.name().equals("US-ASCII")) {
            return input;
        } else {
            Assert.isTrue(charset.name().equals("UTF-8") || charset.name().equals("ISO-8859-1"), "Charset should be UTF-8 or ISO-8859-1");
            byte[] source = input.getBytes(charset);
            int len = source.length;
            StringBuilder sb = new StringBuilder(len << 1);
            sb.append(charset.name());
            sb.append("\'\'");
            byte[] var5 = source;
            int var6 = source.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                byte b = var5[var7];
                if(isRFC5987AttrChar(b)) {
                    sb.append((char)b);
                } else {
                    sb.append('%');
                    char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 15, 16));
                    char hex2 = Character.toUpperCase(Character.forDigit(b & 15, 16));
                    sb.append(hex1);
                    sb.append(hex2);
                }
            }

            return sb.toString();
        }
    }

    private static boolean isRFC5987AttrChar(byte c) {
        return c >= 48 && c <= 57 || c >= 97 && c <= 122 || c >= 65 && c <= 90 || c == 33 || c == 35 || c == 36 || c == 38 || c == 43 || c == 45 || c == 46 || c == 94 || c == 95 || c == 96 || c == 124 || c == 126;
    }
}

