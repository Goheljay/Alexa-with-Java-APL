package com.example.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.example.alexa.handler.*;

public class DemoApplication  extends SkillStreamHandler {

	private static Skill getSkill() {
		return Skills.standard()
				.addRequestHandlers(
						new LaunchHandler(),
						new NextIntentHandler(),
						new WhatUserNameHandler(),
						new FallBackHandler(),
						new PlayMusicHandler(),
						new PlayBackNearlyFinishedRequest(),
						new PlaySongWayHandler(),
						new YesIntentHandler(),
						new NoIntentHandler()
				)
				.build();
	}

	public DemoApplication() {
		super(getSkill());
	}
}
