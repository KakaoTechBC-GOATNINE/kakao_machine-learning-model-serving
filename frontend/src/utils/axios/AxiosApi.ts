import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
  CreateAxiosDefaults,
} from 'axios';
import { MetlifeResBody } from '@/model/common/response/MetlifeResBody';

type RequestType = 'POST' | 'GET' | 'DELETE' | 'PUT';

export const createInstance = (axiosConfig?: CreateAxiosDefaults) =>
  axios.create({
    // baseURL: import.meta.env.VITE_APP_BASEURL,
    headers: {
      'Content-Type': 'application/json',
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Credentials': true,
    },
    transformResponse: parseJson,
    ...axiosConfig,
  } as CreateAxiosDefaults);

function parseJson(res: string): JSON | string {
  let parsedData;

  try {
    parsedData = JSON.parse(res);
  } catch (e) {
    console.log('Error occurred when parsing response data. \n', e);
    return res;
  }

  return parsedData;
}

class AxiosApi {
  instance: AxiosInstance;

  constructor(config?: CreateAxiosDefaults) {
    this.instance = createInstance(config);
  }

  async get<T>(url: string, config?: AxiosRequestConfig<T>): Promise<MetlifeResBody<T>> {
    const result = await this.request<T>('GET', url, undefined, config);
    return result.data;
  }

  async post<T, R = T>(
    uri: string,
    data?: T,
    config?: AxiosRequestConfig<T>,
  ): Promise<MetlifeResBody<R>> {
    const result = await this.request<T, R>('POST', uri, data ?? ({} as T), config);
    return result.data;
  }

  async put<T, R = T>(
    uri: string,
    data?: T,
    config?: AxiosRequestConfig,
  ): Promise<MetlifeResBody<R>> {
    const result = await this.request<T, R>('PUT', uri, data ?? ({} as T), config);
    return result.data;
  }

  async delete<T>(url: string, config?: AxiosRequestConfig<T>): Promise<MetlifeResBody<T>> {
    const result = await this.request<T>('GET', url, undefined, config);
    return result.data;
  }

  private async request<T, R = T>(
    requestType: RequestType,
    url: string,
    data?: T,
    config?: AxiosRequestConfig,
  ) {
    return this.instance.request<T, AxiosResponse<MetlifeResBody<R>>>({
      method: requestType,
      url,
      data,
      ...config,
    });
  }
}

const axiosApi = new AxiosApi();

export default axiosApi;
