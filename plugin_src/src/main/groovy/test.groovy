import sailpoint.api.SailPointContext;
import java.text.SimpleDateFormat;
import sailpoint.object.AuditEvent;
import sailpoint.api.SailPointFactory;
import sailpoint.connector.ExpiredPasswordException;
import sailpoint.object.Identity;
import sailpoint.object.IdentitySelector;
import sailpoint.object.IdentityTrigger;
import sailpoint.object.QueryOptions;
import sailpoint.object.Rule;
import sailpoint.spring.SpringStarter;
import sailpoint.tools.GeneralException;
import sailpoint.tools.JsonHelper;


import java.util.*;
SpringStarter starter = new SpringStarter("iiqBeans"); 
starter.start();
SailPointContext context = SailPointFactory.createContext();

iterator = context.search("from IdentityEntitlement ie join ie.identity", null ,null);

List<Object> all = new ArrayList<Object>();

while(iterator.hasNext()){
    // 
    v = iterator.next()
    
    if(!v.getClass().isArray()){
        v = [v]
    }

    all.add(v);
}

println (JsonHelper.toJson(all));

System.exit(0)
