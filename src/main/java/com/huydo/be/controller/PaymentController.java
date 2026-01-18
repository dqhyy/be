package com.huydo.be.controller;

import com.huydo.be.dto.request.ApiResponse;
import com.huydo.be.entity.Bill;
import com.huydo.be.repository.BillRepository;
import com.huydo.be.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnPayService;
    private final BillRepository billRepository;

    @PostMapping("/create_payment_url")
    public ApiResponse<String> createPaymentUrl(@RequestParam Long billId, HttpServletRequest request) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        if (bill.getStatus() == com.huydo.be.enums.BillStatus.PAID) {
            throw new RuntimeException("Bill already paid");
        }

        long amount = bill.getAmount().longValue();
        String bankCode = request.getParameter("bankCode");

        String paymentUrl = vnPayService.createPaymentUrl(billId, amount, bankCode, request);
        return ApiResponse.<String>builder()
                .result(paymentUrl)
                .build();
    }

    @GetMapping("/vnpay-return")
    public ApiResponse<Integer> vnpayReturn(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);
        return ApiResponse.<Integer>builder()
                .result(paymentStatus)
                .build();
    }
}
