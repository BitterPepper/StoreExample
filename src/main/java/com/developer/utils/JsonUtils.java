package com.developer.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Class was taken from my home library. I am using it when work with Json
 * and adding new universal features when need.
 * 
 * @author sadovskiy
 *
 */
public class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper()
			// enabled to avoid scientific notation when serializing BigDecimal
			.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN)
			// enabled for interoperability wiht XML-to-JSON converters that
			// can mark a field with a single object as a JSON object field,
			// even though it is (could be) an array
			.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
			// omit null fields from output should be covered by NON_EMPTY
			//.setSerializationInclusion(Include.NON_NULL)
			// omit empty fields from output
			.setSerializationInclusion(Include.NON_EMPTY)
			// ignore unknown properties
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			// empty strings will be read as null
			.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
			//.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
			.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
			;
	private static final ObjectWriter writer = mapper.writer();
	@SuppressWarnings("rawtypes")
	private static final Map<Class,ObjectReader> readers = new HashMap<>();
	
	private JsonUtils() {}
	
	public static final String EMPTY_JSON_STRING = "{}";
	public static final Object EMPTY_JSON_OBJECT = Collections.EMPTY_MAP;
	public static final JsonNode EMPTY_JSON_NODE = setEmptyJsonNode();
	
	/**
	 * Use <code>toJson()</code> if you simply need to produce a JSON String
	 * from an object.
	 * 
	 * @return Jackson ObjectWriter
	 */
	public static ObjectWriter getWriter() {
		return writer;
	}
	
	/**
	 * Use <code>readValue()</code> if you simply need to convert a JSON String
	 * to an object.
	 * 
	 * @param clazz - class for which a reader is required
	 * @return thread-safe and reusable <code>ObjectReader</code>
	 */
	public static ObjectReader getReader(Class<?> clazz) {
		ObjectReader reader = readers.get(clazz);
		if (reader == null) {
			reader = mapper.reader(clazz);
			readers.put(clazz, reader);
		}
		return reader;
	}
	
	/**
	 * Converts a JSON String to an object if possible.
	 * 
	 * @param <T> class of the object returned by the method
	 * @param clazz class of the object that JSON is supposed to represent
	 * @param node parse {@link JsonNode} object
	 * @return an instance of the object of type <code>T</code>
	 * @throws IOException if parsing failed
	 */
	public static <T> T readNodeValue(Class<T> clazz, JsonNode node) throws IOException {
		return getReader(clazz).readValue(node);
	}
	
	/**
	 * Too cumbersome and low-level. In practice we should never use it.
	 * It is used, however, by {@link KafkaMessage} to encapsulate content
	 * body and to make it easier to work with it.
	 * 
	 * @param json - incoming JSON String
	 * @return - {@link JsonNode} object
	 * @throws IOException if parsing failed
	 */
	public static JsonNode readTree(String json) throws IOException {
		return mapper.readTree(json);
	}
	
	/**
	 * Produces a String JSON representing an object if possible.
	 * 
	 * @param object the object you want to convert to JSON
	 * @return String representation of the object JSON
	 * @throws IOException if serialization fails
	 */
	public static String toJson(Object object) throws IOException {
		return writer.writeValueAsString(object);
	}
	
	/**
	 * Converts a JSON String to an object if possible.
	 * 
	 * @param <T> class of the object returned by the method
	 * @param clazz class of the object that JSON is supposed to represent
	 * @param json JSON String representing an object
	 * @return an instance of the object of type <code>T</code> or null
	 * if json is null or empty
	 * @throws IOException if parsing failed
	 */
	public static <T> T readValue(Class<T> clazz, String json) throws IOException {
		if (json == null || json.isEmpty()) return null;
		return getReader(clazz).readValue(json);
	}
	
	private static JsonNode setEmptyJsonNode() {
		JsonNode jn = null;
		try {
			jn = readValue(JsonNode.class, EMPTY_JSON_STRING);
		} catch (IOException e) {
			// should be impossible with the proper empty JSON string
			// suppressing exception
			e.printStackTrace();
		}
		return jn;
	}
	
	/**
	 * Will apply patch JSON (JSON carrying only patch info)
	 * to source JSON.
	 * 
	 * @param target JSON to which the patch is to be applied
	 * @param patch JSON carrying updates
	 * @return patched JSON
	 * @throws IOException if parsing fails
	 */
	public static String merge(String target, String patch) throws IOException {
		if (target == null) return patch;
		if (patch == null || patch.isEmpty()) return target;

		JsonNode sNode = readTree(target);
		JsonNode pNode = readTree(patch);
		
		sNode = merge(sNode, pNode);
		return sNode.toString();
	}
	
	public static <T> T merge(String target, String patch, Class<T> clazz) throws IOException {
		if (target == null) return readValue(clazz, patch);
		if (patch == null || patch.isEmpty()) return readValue(clazz, target);

		JsonNode sNode = readTree(target);
		JsonNode pNode = readTree(patch);
		
		sNode = merge(sNode, pNode);
		return readNodeValue(clazz, sNode);
	}
	
	public static <T,P,C> C merge(T target, P patch, Class<C> clazz) throws IOException {
		if (target == null) return readValue(clazz, toJson(patch));
		if (patch == null) return readValue(clazz, toJson(target));
		return merge(toJson(target), toJson(patch), clazz);
	}
	
	public static <T,C> C clone(T target, Class<C> clazz) throws IOException {
		if (target == null) return null;
		return readValue(clazz, toJson(target));
	}
	
	public static JsonNode merge(JsonNode target, JsonNode patch) {
		if (target == null) return patch;
		
		if (patch.isObject()) {
			Iterator<String> patchedFields = patch.fieldNames();
			while (patchedFields.hasNext()) {
				String field = patchedFields.next();
				JsonNode pValue = patch.get(field);
				if (target.isObject()) {
					ObjectNode tobj = (ObjectNode) target;
					if (pValue.isNull()) 
						tobj.remove(field);
					else 
						tobj.set(field, merge(tobj.get(field), pValue));
				}
			}
			return target;
		} else {
			return patch;
		}
	}
	
	public static String buildDiff(String source, String patched) throws IOException {
		if (source == null || source.isEmpty()) source = EMPTY_JSON_STRING;
		if (patched == null || patched.isEmpty()) patched = EMPTY_JSON_STRING;
		
		JsonNode sNode = readTree(source);
		JsonNode tNode = readTree(patched);
		JsonNode diff = buildDiff(sNode, tNode);
		
		return diff.toString();
	}
	
	public static JsonNode buildDiff(JsonNode source, JsonNode patched) {
		if (source.isObject()) {
			ObjectNode sobj = (ObjectNode) source;
			ObjectNode pobj = (ObjectNode) patched;
			
			Set<String> sFields = getNonNullFields(sobj);
			Set<String> pFields = getNonNullFields(pobj);
			
			Set<String> removedFields = new HashSet<>(sFields);
			removedFields.removeAll(pFields);
			Set<String> addedFields = new HashSet<>(pFields);
			addedFields.removeAll(sFields);
			Set<String> updatedFields = new HashSet<>(sFields);
			updatedFields.retainAll(pFields);
			
			ObjectNode patch = mapper.createObjectNode();
			for (String f : removedFields) {
				patch.set(f, null);
			}
			for (String f : addedFields) {
				patch.set(f, pobj.get(f));
			}
			for (String f : updatedFields) {
				JsonNode sValue = sobj.get(f);
				JsonNode pValue = pobj.get(f);
				if (sValue.equals(pValue)) continue;
				JsonNode diff = pValue.isObject() ? buildDiff(sValue, pValue) : pValue;
				if (diff != null) 
					patch.set(f, diff);
			}
			return patch;
		}
		else {
			JsonNode diff = source.equals(patched) ? null : patched;
			return diff;
		}
	}
	
	/**
	 * @param source source node
	 * @param patched patched node
	 * @param includedFields fields, that will be compared. From this set will be excluded fields from excluded Set
	 * @param excludedFields fields, that will be excluded from comparing.
	 * @return detailed difference as a Jackson JsonNode object
	 */
	public static JsonNode buildDiffDetailed(JsonNode source, JsonNode patched, Set<String> includedFields, Set<String> excludedFields) {
		if (source.isObject()) {
			ObjectNode sobj = (ObjectNode) source;
			ObjectNode pobj = (ObjectNode) patched;
			
			Set<String> sFields = getNonNullFields(sobj);
			Set<String> pFields = getNonNullFields(pobj);
			
			Set<String> removedFields = new HashSet<>(sFields);
			removedFields.removeAll(pFields);
			Set<String> addedFields = new HashSet<>(pFields);
			addedFields.removeAll(sFields);
			Set<String> updatedFields = new HashSet<>(sFields);
			updatedFields.retainAll(pFields);
			
			ObjectNode patch = mapper.createObjectNode();
			for (String f : removedFields) {
				patch.set(f+"::removed", sobj.get(f));
			}
			for (String f : addedFields) {
				patch.set(f+"::added", pobj.get(f));
			}
			for (String f : updatedFields) {
				if ((includedFields != null && !includedFields.contains(f))) continue;
				else if (excludedFields != null && excludedFields.contains(f)) continue;
					
				JsonNode sValue = sobj.get(f);
				JsonNode pValue = pobj.get(f);
				if (sValue.equals(pValue)) continue;
		
				JsonNode diff;
				if(pValue.isObject()){
					diff = buildDiffDetailed(sValue, pValue, null, null);
				}else{
					ObjectNode values = mapper.createObjectNode();
					values.set("previous", sValue);
					values.set("current", pValue);
					diff = values;
				}
				patch.set(f+"::modified", diff);
			}
			return patch;
		}
		else {
			JsonNode diff;
			if (source.equals(patched)) {
				diff = null;
			}else{
				ObjectNode values = mapper.createObjectNode();
				values.set("previous", source);
				values.set("current", patched);
				diff = values;
			}
			return diff;
		}
	}

	private static Set<String> getNonNullFields(ObjectNode obj) {
		if (obj == null) return Collections.emptySet();
		
		Set<String> fields = new HashSet<>();
		Iterator<String> it = obj.fieldNames();
		while (it.hasNext()) {
			String field = it.next();
			if (obj.get(field) != null) fields.add(field);
		}
		return fields;
	}
}