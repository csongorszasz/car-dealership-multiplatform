package edu.bbte.idde.scim2304.backend.repository.mysql;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.AdvertDao;
import edu.bbte.idde.scim2304.backend.repository.DaoFactory;

public class MySQLDaoFactory extends DaoFactory {
    private AdvertMySQLDao advertDaoInstance;

    @Override
    public AdvertDao getAdvertDao() {
        if (advertDaoInstance == null) {
            advertDaoInstance = new AdvertMySQLDao(Advert.class);
            MySQLHelper.initializeTable(advertDaoInstance);
        }
        return advertDaoInstance;
    }
}
