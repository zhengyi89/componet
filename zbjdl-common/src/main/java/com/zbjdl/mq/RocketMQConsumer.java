package com.zbjdl.mq;

import java.util.Set;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.google.common.collect.Sets;

/**
 * RocketMQ 消费者
 * 
 *
 */
public class RocketMQConsumer {
	private DefaultMQPushConsumer consumer;
	private Set<String> topics = Sets.newHashSet();
	
	public RocketMQConsumer(String consumerGroup,String nameServAddr) {
		consumer = new DefaultMQPushConsumer(consumerGroup);
		consumer.setNamesrvAddr(nameServAddr);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
	}
	
	public synchronized void subscribe(String topic, MessageListenerConcurrently msg) throws MQClientException {
        //订阅topic下全部Tag的消息
		if(!topics.contains(topic)){
			consumer.subscribe(topic, "*");
			topics.add(topic);
			
		}
        consumer.registerMessageListener(msg);
        consumer.start();
	}
	
	@Override
	protected void finalize() {
		try {
			consumer.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
