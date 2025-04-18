import React, { useRef, useEffect, useState } from "react";

export default function QueryInput({ value, onChange, ...props }) {
  const textareaRef = useRef(null);

  const resizeTextarea = () => {
    const textarea = textareaRef.current;
    if (textarea) {
      textarea.style.height = "auto"; // Reset height
      textarea.style.height = `${textarea.scrollHeight}px`; // Set to scroll height
    }
  };

  useEffect(() => {
    resizeTextarea();
  }, [value]);

  return (
    <textarea
      ref={textareaRef}
      value={value}
      onChange={onChange}
      rows={1}
      style={{
        width: "25vw",
        resize: "none",
        overflow: "hidden",
        fontSize: "1.2rem",
        lineHeight: "1.3",
        padding: "0.5rem",
        margin: "2em",
        backgroundColor: "var(--triary)",
        color: "var(--primary)",
        border: "1px solid var(--secondary)",
        borderRadius: "5px",
        outline: "none",
      }}
      {...props}
    />
  );
}
