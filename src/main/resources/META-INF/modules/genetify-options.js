GENETIFY_CONFIG = {       

        // Request remote script that contains results for variants in the
        // current page. 
        REQUEST_RESULTS: true,

        // Request results as a static file from a cache on the server.
        USE_RESULTS_CACHE: true,

        // Force current genes to variants previously viewed by visitor.
        // Useful for ensuring a consistent user experience.
        USE_COOKIE: false,                               

        // Namespace in which encountered genes are placed. Useful for
        // treating genes on different pages as a single unit.
        NAMESPACE: window.location.pathname,

        // Skip all initialization for varying genes. Useful on pages with
        // goals but no genes.
        NO_VARYING: false,       

        // Load graphical control panel for testing.
        LOAD_CONTROLS: true

    }