import React from "react";

const OTPInput = ({ otp, setOtp }) => {
  const handleChange = (e) => {
    let value = e.target.value;
    if (/[^0-9]/.test(value)) return; // Allow only digits

    if (value.length <= 6) {
      setOtp(value);
    }
  };

  return (
    <div>
      <input
        type="text"
        value={otp}
        onChange={handleChange}
        maxLength="6"
        placeholder="Enter OTP"
        style={{ width: "8em", fontSize: "1.8rem", textAlign: "center" }}
      />
    </div>
  );
};

export default OTPInput;
