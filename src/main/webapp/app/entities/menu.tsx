import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/masterdata">
        <Translate contentKey="global.menu.entities.masterdata" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/khach-hang">
        <Translate contentKey="global.menu.entities.khachHang" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/danh-muc-hang">
        <Translate contentKey="global.menu.entities.danhMucHang" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/phieu-nhap-xuat">
        <Translate contentKey="global.menu.entities.phieuNhapXuat" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chi-tiet-nhap-xuat">
        <Translate contentKey="global.menu.entities.chiTietNhapXuat" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
