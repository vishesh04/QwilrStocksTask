package qwilr

import groovyx.net.http.RESTClient

class StockApi {

    final static def API_ENDPOINT  = 'http://dev.markitondemand.com'

    static def lookup(String query) {
        def restClient = new RESTClient(API_ENDPOINT)
        return restClient.get(path: '/MODApis/Api/v2/Lookup/json', query: [input: query]).data
    }

    static def getQuote(String symbol) {
        def restClient = new RESTClient(API_ENDPOINT)
        return restClient.get(path: '/MODApis/Api/v2/Quote/json', query: [symbol: symbol]).data
    }
}
