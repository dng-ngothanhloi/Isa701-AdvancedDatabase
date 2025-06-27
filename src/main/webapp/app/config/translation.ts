import { Storage, TranslatorContext } from 'react-jhipster';

import { setLocale } from 'app/shared/reducers/locale';

TranslatorContext.setDefaultLocale('vi');
TranslatorContext.setRenderInnerTextForMissingKeys(false);

export const languages: any = {
  vi: { name: 'Tiếng Việt' },
  en: { name: 'English' },
  ja: { name: '日本語' },
  // jhipster-needle-i18n-language-key-pipe - JHipster will add/remove languages in this object
};

export const locales = Object.keys(languages).sort();

export const registerLocale = store => {
  store.dispatch(setLocale(Storage.session.get('locale', 'vi')));
};
