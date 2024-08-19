import React from "react";
import Button from "@mui/material/Button";

export default function CustomButton({ text, color }) {
    return (
        <Button color={color}>
            {text}
        </Button>
    );
}