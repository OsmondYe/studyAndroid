package com.osmond.study.onedrive;

/**
 * An OAuth Request that can be observed, by adding observers that will be notified on any
 * exception or response.
 */
interface ObservableOAuthRequest {
    /**
     * Adds an observer to observe the OAuth request
     *
     * @param observer to add
     */
    void addObserver(OAuthRequestObserver observer);

    /**
     * Removes an observer that is observing the OAuth request
     *
     * @param observer to remove
     * @return true if the observer was removed.
     */
    boolean removeObserver(OAuthRequestObserver observer);
}