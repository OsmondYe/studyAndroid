package com.osmond.study.onedrive;

/**
 * An observer of an OAuth Request. It will be notified of an Exception or of a Response.
 */
interface OAuthRequestObserver {
    /**
     * Callback used on an exception.
     *
     * @param exception
     */
    void onException(LiveAuthException exception);

    /**
     * Callback used on a response.
     *
     * @param response
     */
    void onResponse(OAuthResponse response);
}
