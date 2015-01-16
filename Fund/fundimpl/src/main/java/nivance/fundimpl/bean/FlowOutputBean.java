package nivance.fundimpl.bean;

import java.util.List;

import lombok.Data;

import org.apache.avro.reflect.Nullable;

public @Data class FlowOutputBean {
    /** 查询出的总条数*/
	@Nullable
	private int size;
    /**返回的资金流水list*/	
	@Nullable
	private List<FlowBean> flowoutputbean;

}
