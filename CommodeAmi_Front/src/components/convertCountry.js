const convertCountry = (countryCode) => {
    const countryMapping = {
        'KR': '한국',
        'US': '미국',
        'AU': '호주',
        'GB': '영국',
        'FR': '프랑스',
        'JP': '일본', 
        'CL': '칠레',
        'DE': '독일',
        'DK': '덴마크',
        'NO': '노르웨이',
        'SE': '스웨덴',
        'MN': '몽골',
        'CH': '스위스',
        'ES': '스페인',
        'ID': '인도네시아',
        'BR': '브라질',
        'TR': '터키',
        'MX': '멕시코',
        'AR': '아르헨티나',
        'IT': '이탈리아',
        'LT': '리투아니아',
        'PH': '필리핀',
        'TW': '대만',
        'IE': '아일랜드',
        'CN': '중국'
    };
    return countryMapping[countryCode] || countryCode;
};

export default convertCountry;
