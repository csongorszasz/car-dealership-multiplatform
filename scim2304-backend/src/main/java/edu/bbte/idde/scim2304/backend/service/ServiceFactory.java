package edu.bbte.idde.scim2304.backend.service;

public class ServiceFactory {
    private static AdvertService advertService;

    public static synchronized AdvertService getAdvertService() {
        if (advertService == null) {
            advertService = new AdvertServiceImpl();
        }
        return advertService;
    }
}
