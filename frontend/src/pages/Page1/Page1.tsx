import Typography from '@mui/material/Typography';

import Meta from '@/components/Meta';
import { FullSizeCenteredFlexBox } from '@/components/styled';
import { useEffect } from 'react';
import useDialog from '@/hooks/useDialog';
import { Button, Grid } from '@mui/material';

function Page1() {
  const { alert, confirm } = useDialog();

  useEffect(() => {
    console.log(import.meta.env.VITE_APP_ENVIRONMENT);
  }, []);

  const handleClickAlert = async () => {
    console.log('do something...');
    await alert('console 창 켠 후 확인');
    console.log('do something after click ok');
  };

  const handleClickConfirm = async () => {
    console.log('do something...');
    const confirmed = await confirm('console 창 켠 후 확인');

    if (confirmed) {
      console.log('do something after click ok');
    } else {
      console.log('return');
    }
  };

  return (
    <>
      <Meta title="page 1" />
      <FullSizeCenteredFlexBox>
        <Grid container spacing={1} justifyContent="center">
          <Grid item md={10}>
            <Typography variant="h3">Page 1</Typography>
          </Grid>
          <Grid item md={10}>
            <Button variant="outlined" onClick={handleClickAlert}>
              alert
            </Button>
          </Grid>
          <Grid item md={10}>
            <Button variant="outlined" onClick={handleClickConfirm}>
              confirm
            </Button>
          </Grid>
        </Grid>
      </FullSizeCenteredFlexBox>
    </>
  );
}

export default Page1;
