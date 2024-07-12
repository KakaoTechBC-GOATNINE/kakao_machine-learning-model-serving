import React from 'react';
import { Button, Dialog, DialogActions, DialogContent, DialogProps } from '@mui/material';

interface Props {
  content: string;
  dialogProps?: DialogProps;
  onConfirm: () => void;
  onClose: () => void;
}

const Alert: React.FC<Props> = ({ content, dialogProps, onConfirm, onClose }) => {
  const handleConfirm = () => {
    onConfirm();
    onClose();
  };

  const handleClose = () => {
    onClose();
  };

  return (
    <Dialog open={true} onClose={handleClose} {...dialogProps} fullWidth>
      <DialogContent>{content}</DialogContent>
      <DialogActions>
        <Button onClick={handleConfirm}>확인</Button>
      </DialogActions>
    </Dialog>
  );
};

export default Alert;
