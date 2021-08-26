package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.util.JsonUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

    private static final String IEX_BATCH_PATH = "stock/market/batch?symbols=%s&types=quote&token=";
    private final String IEX_BATCH_URL;

    private final Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
    private final HttpClientConnectionManager httpClientConnectionManager;

    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
                         MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
    }


    @Override
    public <S extends IexQuote> S save(S ticker) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> tickers) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get an IexQuote (helper method which class findAllById)
     *
     * @param ticker
     * @throws IllegalArgumentException      if a given ticker is invalid
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    @Override
    public Optional<IexQuote> findById(String ticker) {
        Optional<IexQuote> iexQuote;
        List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

        if (quotes.size() == 0) {
            return Optional.empty();
        } else if (quotes.size() == 1) {
            iexQuote = Optional.of(quotes.get(0));
        } else {
            throw new DataRetrievalFailureException("Unexpected number of quotes");
        }
        return iexQuote;
    }

    @Override
    public boolean existsById(String ticker) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Iterable<IexQuote> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Get quotes from IEX
     *
     * @param tickers is a ist of tickers
     * @throws IllegalArgumentException      if ticker is invalid or tickers is emtpy
     * @throws DataRetrievalFailureException if HTTP request failed
     */
    @Override
    public List<IexQuote> findAllById(Iterable<String> tickers) {
        if (((Collection<?>) tickers).isEmpty()) {
            throw new IllegalArgumentException("ERROR: List of tickers is empty");
        }
        String url = String.format(IEX_BATCH_URL, String.join(",", tickers));
        Optional<String> response = executeHttpGet(url);
        List<IexQuote> quotes = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response.get());
        } catch (JSONException e) {
            logger.error("Unable to create json object");
        }
        for (String ticker : tickers) {
            if (!jsonObject.has(ticker)) {
                throw new IllegalArgumentException("Ticker value is invalid");
            }
            try {
                quotes.add(JsonUtil
                        .toObjectFromJson(jsonObject.getJSONObject(ticker).getJSONObject("quote").toString(),
                                IexQuote.class));
            } catch (IOException | JSONException ex) {
                logger.error("Failed to convert JSON String to object", ex);
            }
        }
        return quotes;
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteById(String ticker) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void delete(IexQuote iexQuote) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll(Iterable<? extends IexQuote> tickers) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Execute a get and return http/entity body as a String
     * <p>
     * Tip: Use EntitiyUtils.toString to process HTTP entity
     *
     * @param url resource url
     * @return http response body or Optional.empty for 404 responses
     * @throws DataRetrievalFailureException if HTTP failed or status is unexpected
     */
    private Optional<String> executeHttpGet(String url) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpUriRequest httpRequest = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return Optional.of(EntityUtils.toString(httpResponse.getEntity()));
            } else if (statusCode == 404) {
                return Optional.empty();
            } else {
                throw new DataRetrievalFailureException("Unexpected status code");
            }
        } catch (IOException ex) {
            throw new DataRetrievalFailureException("HTTP request failed");
        }
    }

    /**
     * Borrow a HTTP Client from the httpClientConnectionManager
     *
     * @return a httpClient
     */
    private CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .setConnectionManagerShared(true)
                .build();
    }
}
