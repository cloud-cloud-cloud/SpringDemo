package com.example.demo.components.queue;

public interface MsgListener {

	ConsumeResultAction consume(ConsumeContext context);
}
