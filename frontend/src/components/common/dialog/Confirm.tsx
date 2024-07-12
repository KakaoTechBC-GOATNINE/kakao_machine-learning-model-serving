import React from 'react';
import { Button, Dialog, DialogActions, DialogContent, DialogProps } from '@mui/material';

interface Props {
  content: string;
  dialogProps?: DialogProps;
  onConfirm: (value: boolean) => void;
  onClose: () => void;
}

const Confirm: React.FC<Props> = ({ content, dialogProps, onClose, onConfirm }) => {
  const handleConfirm = () => {
    onConfirm(true);
    onClose();
  };

  const handleClose = () => {
    onConfirm(false);
    onClose();
  };

  return (
    <Dialog open={true} onClose={onClose} {...dialogProps} fullWidth>
      <DialogContent>{content}</DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>취소</Button>
        <Button onClick={handleConfirm}>확인</Button>
      </DialogActions>
    </Dialog>
  );
};

export default Confirm;
