package org.egc.ows.request.csw;

import com.google.common.base.Joiner;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.opengis.csw.v_2_0_2.ResultType;

/**
 * @author houzhiwei
 * @date 2021/7/7 17:27
 */
@Slf4j
@Data
public class GetRecordsRequest {

    private String namespace;

    private String requestId;

    private String outputFormat = "application/xml";
    private String outputSchema;

    private int startPosition = 1;

    private int maxRecords = 10;

    private String typeNames;


    private String elementSetName;


    private String elementSetNameTypeName;


    private String elementName;


    private String sortBy;

    private Boolean distributedSearch = false;


    private int hopCount = 2;


    private String clientId;


    private String distributedSearchId;


    private long distributedSearchIdTimout = 600;


    private String federatedCatalogues;


    private String responseHandler;
    private String constraintLanguage;
    //1.1.0
    private String constraintLanguageVersion;
    private String constraint;

    private ResultType resultType;

    //advanced query parameters
    public final static String CONSTRAINT_LANGUAGE = "constraintLanguage";
    public final static String CONSTRAINT_LANGUAGE_VERSION = "constraint_language_version";
    public final static String CONSTRAINT = "Constraint";

    private GetRecordsRequest(Builder builder) {
        setNamespace(builder.namespace);
        setRequestId(builder.requestId);
        setOutputFormat(builder.outputFormat);
        setOutputSchema(builder.outputSchema);
        setStartPosition(builder.startPosition);
        setMaxRecords(builder.maxRecords);
        setTypeNames(builder.typeNames);
        setElementSetName(builder.elementSetName);
        setElementSetNameTypeName(builder.elementSetNameTypeName);
        setElementName(builder.elementName);
        setSortBy(builder.sortBy);
        setDistributedSearch(builder.distributedSearch);
        setHopCount(builder.hopCount);
        setClientId(builder.clientId);
        setDistributedSearchId(builder.distributedSearchId);
        setDistributedSearchIdTimout(builder.distributedSearchIdTimout);
        setFederatedCatalogues(builder.federatedCatalogues);
        setResponseHandler(builder.responseHandler);

        setConstraint(builder.constraint);
        setConstraintLanguage(builder.constraintLanguage);
        setConstraintLanguageVersion(builder.constraintLanguageVersion);

        setResultType(builder.resultType);
    }


    public static final class Builder {
        private String namespace = CSWRequestBuilder.CSW_202_NS;
        private String requestId;
        private String outputFormat = CSWRequestBuilder.FORMAT_XML;
        private String outputSchema = CSWRequestBuilder.GMD_NS;
        private int startPosition = 1;
        private int maxRecords = 10;
        private String typeNames = "csw:Record";
        private String elementSetName = "full";
        private String elementSetNameTypeName;
        private String elementName;
        private String sortBy;
        private boolean distributedSearch = false;
        private int hopCount;
        private String clientId;
        private String distributedSearchId;
        private long distributedSearchIdTimout = 600;
        private String federatedCatalogues;
        private String responseHandler;

        private String constraintLanguage = "CQL_TEXT";
        private String constraintLanguageVersion = "1.1.0";
        private String constraint;
        //v2.0.2?
        private ResultType resultType = ResultType.RESULTS;

        public Builder() {
        }


        /**
         * Namespace builder.
         * <pre>
         * List of Character String, comma separated
         * Used to specify a namespace and its prefix
         * Format is xmlns([prefix=]namespace-url). If the prefix is not specified then this is the default namespace.
         * Optional b
         * If qualified names are used in a request all prefixes must be declared by a namespace specification.
         * Only if no qualified name is used the NAMESPACE parameter can be omitted. In this case all elements are in the default namespacef.
         *
         * @param val the val
         * @return the builder
         */
        public Builder namespace(String... val) {
            namespace = Joiner.on(",").join(val);
            return this;
        }

        /**
         * Request id builder.
         * Include when client chooses to assign requestId. This parameter is mandatory in the case of a distributed search.
         * URI
         *
         * @param val the val
         * @return the builder
         */
        public Builder requestId(String val) {
            requestId = val;
            return this;
        }

        /**
         * Output format builder.
         * Value is Mime type
         * For SOAP application/soap+xml is mandatory.
         *
         * @param val the val
         * @return the builder
         */
        public Builder outputFormat(String val) {
            outputFormat = val;
            return this;
        }

        /**
         * Output schema builder.
         * e.g., http://www.isotc211.org/2005/gmd ({@link CSWRequestBuilder#GMD_NS})
         *
         * @param val the val, e.g., {@link CSWRequestBuilder#GMD_NS}
         * @return the builder
         */
        public Builder outputSchema(String val) {
            outputSchema = val;
            return this;
        }

        /**
         * Start position builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder startPosition(int val) {
            startPosition = val;
            return this;
        }

        /**
         * Max records builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder maxRecords(int val) {
            maxRecords = val;
            return this;
        }

        /**
         * Type names builder.
         * <p>
         * List of Character String, comma separated
         * Unordered List of object types implicated in the query
         * Mandatory
         * e.g., csw:Record,gmd:MD_Metadata
         *
         * @param val the val
         * @return the builder
         */
        public Builder typeNames(String... val) {
            typeNames = Joiner.on(",").join(val);
            return this;
        }

        /**
         * Element set name builder.
         * One (but not both) of  ElementSetName or ElementName must be specified.
         *
         * @param val the val
         * @return the builder
         */
        public Builder elementSetName(String val) {
            elementSetName = val;
            return this;
        }

        /**
         * Element set name type name builder.
         * May only be specified if the ElementSetName parameters is specified.
         *
         * @param val the val
         * @return the builder
         */
        public Builder elementSetNameTypeName(String val) {
            elementSetNameTypeName = val;
            return this;
        }

        /**
         * Element name builder.
         * <pre>
         * List of Character String
         * One (but not both) of  ElementSetName or ElementName must be specified.
         *
         * @param val the val
         * @return the builder
         */
        public Builder elementName(String... val) {
            elementName = Joiner.on(",").join(val);
            return this;
        }

        /**
         * Sort by builder.<pre>
         * List of Character String, comma separated
         * Ordered list of names of metadata elements to use for sorting the response
         * Format of each list item is metadata_element_name:
         * A indicating an ascending sort or metadata_ element_name:D indicating descending sort
         * Default action is to sort the result according to its default sort which must be declared in the capabilities document (see DefaultSortAlgorithm, Table 19).
         * If no default sort is specified in the capabilities document then it is assumed that the server will sort responses alphabetically by Title (see 6.6.3) in ascending order.
         * If names of one or more metadata elements are provided ordering is done as follows: first order is based on first metadata-element (order: A or D), if records exist with the same value for the first meta-data element ordering of those records is done based on second metadata-element (order: A or D), and so on...
         * sortField and  sortOrder
         *
         * @param val the val
         * @return the builder
         */
        public Builder sortBy(String... val) {
            sortBy = Joiner.on(",").join(val);
            return this;
        }

        /**
         * Distributed search builder.
         *
         * @param val the val
         * @return the builder
         */
        public Builder distributedSearch(Boolean val) {
            distributedSearch = val;
            return this;
        }

        /**
         * Hop count builder.
         * Include only if DistributedSearch parameter is included
         *
         * @param val the val
         * @return the builder
         */
        public Builder hopCount(Integer val) {
            hopCount = val;
            return this;
        }

        /**
         * Client id builder.<pre>
         * Any URI
         * Mandatory if DistributedSearch set to TRUE
         *
         * @param val the val
         * @return the builder
         */
        public Builder clientId(String val) {
            clientId = val;
            return this;
        }

        /**
         * Distributed search id builder.
         * <p>
         * Any URI
         * Mandatory if DistributedSearch set to TRUE
         *
         * @param val the val
         * @return the builder
         */
        public Builder distributedSearchId(String val) {
            distributedSearchId = val;
            return this;
        }

        /**
         * Distributed search id timout builder.
         * This timeout parameter defines (in sec) how long the distributedSearchId should be valid, meaning how long a server involved in distributed search should minimally store information related to the distributedSearchId
         * Makes only sense in case that DistributedSearch set to TRUE
         *
         * @param val the val
         * @return the builder
         */
        public Builder distributedSearchIdTimout(long val) {
            distributedSearchIdTimout = val;
            return this;
        }

        /**
         * Federated catalogues builder.
         * List of comma separated structures of the format: FederatedCatalogues ( fC(URL1, [timeout1]), fC(URL2, [timeout1]),…fC(URLn, [timeoutn])). The URLs have to be escaped. [] means optional.e
         * Optional if DistributedSearch set to TRUE. Don´t include when DistributedSearch set to FALSE
         *
         * @param val the val
         * @return the builder
         */
        public Builder federatedCatalogues(String... val) {
            federatedCatalogues = Joiner.on(",").join(val);
            return this;
        }


        /**
         * Response handler builder. <br/>
         * Comma separated list of anyURI <br/>
         * If not included, process request synchronously
         *
         * @param val the val
         * @return the builder
         */
        public Builder responseHandler(String... val) {
            responseHandler = Joiner.on(",").join(val);
            return this;
        }

        /**
         * Identifies the predicate language used for the value of the Constraint
         *
         * @param val the val
         * @return the builder
         */
        public Builder constraintLanguage(String val) {
            constraintLanguage = val;
            return this;
        }

        /**
         * Identifies the version of the predicate language used.
         *
         * @param val the val
         * @return the builder
         */
        public Builder constraintLanguageVersion(String val) {
            constraintLanguageVersion = val;
            return this;
        }

        /**
         * Text of query constraint in the predicate language identified by the CONSTRAINT‌LANGUAGE
         *
         * @param val the val
         * @return the builder
         */
        public Builder constraint(String val) {
            constraint = val;
            return this;
        }

        /**
         * @param val results,hits,validate
         * @return
         */
        public Builder resultType(String val) {
            resultType = ResultType.fromValue(val);
            return this;
        }

        public GetRecordsRequest build() {
            return new GetRecordsRequest(this);
        }
    }
}
