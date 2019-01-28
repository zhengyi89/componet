package com.zbjdl.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * RocketMQ 生产者
 * 
 *
 */
public class RocketMQProducer {
	private static final Logger logger = LoggerFactory.getLogger(RocketMQProducer.class);
	
	private DefaultMQProducer producer;
	
	public RocketMQProducer(String producerGroup,String nameServAddr) throws MQClientException{
		producer = new DefaultMQProducer(producerGroup);
		producer.setNamesrvAddr(nameServAddr);
		logger.info("RocketMQProducer start with producer group:{}, name server address:{}", producerGroup, nameServAddr);
		producer.start();
	}
	
	/**
	 * 发送消息
	 * @param topic
	 * @param data
	 * @param tags 使用对象类名（包括package）
	 * @return
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws MQBrokerException
	 * @throws InterruptedException
	 */
	public SendResult publish(String topic, String tags, byte[] data) throws MQClientException,
			RemotingException, MQBrokerException, InterruptedException {
		logger.info("message publish with topic:{},tags:{}", topic, tags);
		Message msg = new Message(topic, tags, data);
		return producer.send(msg);
	}
	
	@Override
	protected void finalize() {
		try {
			 producer.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
