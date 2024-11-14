package com.project.gamemarket.service;

import com.project.gamemarket.dto.key.KeyActivationRequestDto;
import com.project.gamemarket.dto.key.KeyActivationResponseDto;

public interface KeyActivationService {

    KeyActivationResponseDto processKeyActivation(KeyActivationRequestDto key);
}
