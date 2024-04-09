package edu.java.clients.interfaces;

import org.example.dto.request.SendUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UpdateLinkService {
    void updates(SendUpdateRequest request);
}
