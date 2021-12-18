package com.devatlant.chatbot.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.telegram.telegrambots.api.objects.Message;

import java.io.IOException;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GameTest {
    private Game testSubject;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup(){
        testSubject = new Game(100, new Random());
    }

    @org.junit.jupiter.api.Test
    void should_respond_with_start() {
        Message start = buildMessage("/start");
        ResponseWithCounter res = testSubject.reactOnGamerMessage(start);
        assertEquals(RESPONSE.START, res.code);
    }

    public Message buildMessage(final String text){
        try {
            return objectMapper.readValue(String.format("{\"text\":\"%s\"}", text), Message.class);
        } catch (IOException e) {
           throw new RuntimeException("wrong json syntax for "+ text,e);
        }
    }

    @Test
    public void should_return_answer_with_counter(){
        //given
        testSubject = new Game(100, new Random());
        Message message = buildMessage("1");
        //run
        ResponseWithCounter res = testSubject.reactOnGamerMessage(message);

        // assert
        assertEquals(1, res.counter);
    }

    @ParameterizedTest
    @MethodSource("input_data")
    public void should_return_true_when_input_data_is_integer(String input, boolean expected){
        //given
        testSubject = new Game(100, new Random());

        //run
        boolean result = testSubject.isInteger(input);

        // assert
        assertEquals(expected, result);
    }

    private static Stream<Arguments> input_data() {
        return Stream.of(
                Arguments.of("3", true),
                Arguments.of("1", true),
                Arguments.of("2", false),
                Arguments.of("not blank", false)
        );
    }
}
