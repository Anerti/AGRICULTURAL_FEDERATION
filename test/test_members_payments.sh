#!/bin/bash

BASE_URL="http://localhost:8080"
TEST_COOP_ID="b2c3d4e5-0001-0001-0001-000000000001"
TEST_MEMBER_ID="a1b2c3d4-0001-0001-0001-000000000001"

echo "=== Test: POST /members/{id}/payments ==="

echo ""
echo "--- Test 1: Happy path - Record payment successfully ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 2: Not Found - Member does not exist ---"
curl -s -X POST "${BASE_URL}/members/00000000-0000-0000-0000-000000000000/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 3: Bad Request - Invalid payment mode ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "INVALID_MODE"
  }' | jq .

echo ""
echo "--- Test 4: Bad Request - Missing amount ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 5: Bad Request - Missing membershipFeeIdentifier ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 6: Bad Request - Missing accountCreditedIdentifier ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "membershipFeeIdentifier": "MF-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 7: Bad Request - Missing paymentMode ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 50000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001"
  }' | jq .

echo ""
echo "--- Test 8: Bad Request - Negative amount ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": -100,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 9: Bad Request - Zero amount ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 0,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "CASH"
  }' | jq .

echo ""
echo "--- Test 10: Payment with MOBILE_BANKING mode ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 25000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "MOBILE_BANKING"
  }' | jq .

echo ""
echo "--- Test 11: Payment with BANK_TRANSFER mode ---"
curl -s -X POST "${BASE_URL}/members/${TEST_MEMBER_ID}/payments" \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 100000,
    "membershipFeeIdentifier": "MF-001",
    "accountCreditedIdentifier": "ACC-001",
    "paymentMode": "BANK_TRANSFER"
  }' | jq .