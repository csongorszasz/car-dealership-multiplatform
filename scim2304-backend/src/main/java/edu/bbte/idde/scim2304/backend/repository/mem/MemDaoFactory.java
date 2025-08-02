package edu.bbte.idde.scim2304.backend.repository.mem;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.AdvertDao;
import edu.bbte.idde.scim2304.backend.repository.DaoFactory;

public class MemDaoFactory extends DaoFactory {
    private AdvertMemDao advertDaoInstance;

    @Override
    public AdvertDao getAdvertDao() {
        if (advertDaoInstance == null) {
            advertDaoInstance = new AdvertMemDao(Advert.class);
        }
        return advertDaoInstance;
    }
}
