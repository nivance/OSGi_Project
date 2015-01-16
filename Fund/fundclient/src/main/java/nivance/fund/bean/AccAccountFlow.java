package nivance.fund.bean;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class AccAccountFlow  {
    @Nullable
    private String uuid;
    @Nullable
    private Short type;
    @Nullable
    private BigDecimal amount;
    @Nullable
    private BigDecimal period;
    @Nullable
    private String merchantid;
    @Nullable
    private String ltype;
    @Nullable
    private String orderno;
    @Nullable
    private BigDecimal balance;
    @Nullable
    private Date createdate;
    @Nullable
    private String messageid;
    @Nullable
    private String description;
    @Nullable
    private String externalfield1;
    @Nullable
    private String externalfield2;
}