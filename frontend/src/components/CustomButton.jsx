import React from "react";
import Button from "@mui/material/Button";

export default function CustomButton({ text, variant, onClick, sx }) {
    return (
        <Button variant={variant} fullWidth onClick={onClick} sx={sx}>
            {text}
        </Button>
    );
}