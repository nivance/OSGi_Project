package nivance.jpaclient.test.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import nivance.dbpapi.api.DomainDaoSupport;
import nivance.dbpapi.domain.DomainController;
import nivance.dbpapi.exception.JPAException;
import nivance.dbpapi.spring.JpaProxyFactory;
import nivance.serialize.SerializerUtil;

import org.apache.avro.reflect.Nullable;

@Slf4j
//@Component
public class RedisTest {

	@PostConstruct
	public void doTest() throws IOException, JPAException {
		AwardInfo ai = new AwardInfo();
		Map<String, byte[]> aw = new HashMap<String, byte[]>();
		aw.put("ac001", "123abc".getBytes());
		aw.put("ac002", "123abc".getBytes());
		aw.put("ac003", "123abc".getBytes());
		aw.put("ac004", "123abc".getBytes());
		aw.put("ac005", "123abc".getBytes());
		ai.setGameCode("T0001");
		ai.setBatchNum("chengyang000001");
		ai.setAllPrize(aw);
		
		Object bs = SerializerUtil.serialize(DomainDaoSupport.type, ai);
		
		DomainController dc = new DomainController();
		dc.setNames("award");
		dc.setTarget("redis");
		
		jpaProxyFactory.getDomainDaoSupport().batchInsert(dc, bs);
		
		
		ai = new AwardInfo();
		ai.setGameCode("T0001");
		ai.setBatchNum("chengyang000001");
		ai.setAwardCode("ac001");
		
		bs = SerializerUtil.serialize(DomainDaoSupport.type, ai);
		
		byte[] sss = (byte[])jpaProxyFactory.getDomainDaoSupport().findOne(dc, bs);
		AwardInfo one = SerializerUtil.deserialize(DomainDaoSupport.type, sss, AwardInfo.class);
		
		System.out.println("123".equals(one.getPrizeInfo()));
		System.out.println("abc".equals(one.getPrizeGroup()));
		log.info("aaaaaaaaaaaaaaaaaaaaaaaa" + one.getPrizeInfo());
		log.info("bbbbbbbbbbbbbbbbbbbbbbbb" + one.getPrizeGroup());
		
		
		
		
		

		DomainController dc2 = new DomainController();
		dc2.setNames("prizeLevel");
		dc2.setTarget("redis");
		
		@SuppressWarnings("unchecked")
		List<byte[]> bbbb = (List<byte[]>)jpaProxyFactory.getDomainDaoSupport().findAll(dc2, null);
		
		List<List<PrizeLevelInfo>> list = new ArrayList<List<PrizeLevelInfo>>();
		for(byte[] b : bbbb) {
			list.add(SerializerUtil.deserializeArray(DomainDaoSupport.type, b, PrizeLevelInfo.class));
		}
		log.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxx" + list.get(0).get(0).getGameCode());
		log.info("yyyyyyyyyyyyyyyyyyyyyyyyyyyy" + list.get(0).get(0).getProductionBatch());
	}

	private @Resource(name="jpaProxyFactory") JpaProxyFactory jpaProxyFactory;

	
	@Data
	public static class AwardInfo {
		/** 方案代码*/
		private @Nullable String gameCode;
		/** 批次编号*/
		private @Nullable String batchNum;
		/** 兑奖验证码*/
		private @Nullable String awardCode;
		/** 中奖信息密文*/
		private @Nullable Byte[] prizeInfo;
		/** 奖组*/
		private @Nullable Byte[] prizeGroup;
		/** key为批次编号，value为中奖信息密文+奖组。只在写入数据时使用。*/
		private @Nullable Map<String, byte[]> allPrize;
	}

	@Data
	public static class PrizeLevelInfo {
		/** 方案代码 **/
		private @Nullable String gameCode;
		/** 生产批次 **/
		private @Nullable String productionBatch;
		/** 奖等名称 **/
		private @Nullable String prizeLevelName;
		/** 奖等编号 **/
		private @Nullable String prizeLevelNum;
		/** 奖等类型 **/
		private @Nullable String prizeType;
		/** 奖等金额 **/
		private @Nullable Long prizeAmount;
		/** 奖等数量 **/
		private @Nullable Integer prizeCount;
		/** 创建时间 **/
		private @Nullable Long dataCreateTime;
		/** 印厂编号 **/
		private @Nullable String printerNum;
	}
}
